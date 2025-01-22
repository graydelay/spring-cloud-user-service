package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.Greeting;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody RequestUser user) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = mapper.map(user, UserDto.class);
        UserDto response = userService.createUser(userDto);

        ResponseUser responseUser = mapper.map(response, ResponseUser.class);
        return new ResponseEntity(responseUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity getUsers(Pageable pageable) {
        Page<UserEntity> userPage = userService.getUserByAll(pageable);
        Page<ResponseUser> responsePage = userPage.map(user -> 
            new ModelMapper().map(user, ResponseUser.class)
        );
        
        PagedResponseUser pagedResponse = new PagedResponseUser(responsePage);
        return new ResponseEntity(pagedResponse, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity getUser(@PathVariable("userId") String userId) {
        UserDto userByUserId = userService.getUserByUserId(userId);
        ResponseUser result = new ModelMapper().map(userByUserId, ResponseUser.class);

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity updateUser(@PathVariable("userId") String userId, @RequestBody RequestUser user) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = mapper.map(user, UserDto.class);
        userDto.setUserId(userId);
        UserDto updatedUser = userService.updateUser(userDto);

        ResponseUser responseUser = mapper.map(updatedUser, ResponseUser.class);
        return new ResponseEntity(responseUser, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUser(userId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
