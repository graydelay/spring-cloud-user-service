package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final ModelMapper mapper;

    public UserController(UserService userService) {
        this.userService = userService;
        this.mapper = new ModelMapper();
        this.mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @PostMapping
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
        UserDto userDto = mapper.map(user, UserDto.class);
        UserDto createdUser = userService.createUser(userDto);
        ResponseUser responseUser = mapper.map(createdUser, ResponseUser.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping
    public ResponseEntity<List<ResponseUser>> getUsers(@PageableDefault(size = 10) Pageable pageable) {
        Page<UserEntity> userPage = userService.getUserByAll(pageable);
        List<ResponseUser> result = userPage.getContent().stream()
                .map(user -> mapper.map(user, ResponseUser.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId) {
        UserDto userDto = userService.getUserByUserId(userId);
        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);
        return ResponseEntity.ok(responseUser);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ResponseUser> updateUser(@PathVariable("userId") String userId, 
                                                 @RequestBody RequestUser user) {
        UserDto userDto = mapper.map(user, UserDto.class);
        userDto.setUserId(userId);
        UserDto updatedUser = userService.updateUser(userDto);
        ResponseUser responseUser = mapper.map(updatedUser, ResponseUser.class);
        return ResponseEntity.ok(responseUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
