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
        UserDto response = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.map(response, ResponseUser.class));
    }

    @GetMapping
    public ResponseEntity<Page<ResponseUser>> getUsers(@PageableDefault(size = 10) Pageable pageable) {
        Page<UserEntity> userPage = userService.getUserByAll(pageable);
        Page<ResponseUser> responsePage = userPage.map(user -> mapper.map(user, ResponseUser.class));
        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId) {
        UserDto userByUserId = userService.getUserByUserId(userId);
        return ResponseEntity.ok(mapper.map(userByUserId, ResponseUser.class));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ResponseUser> updateUser(@PathVariable("userId") String userId, @RequestBody RequestUser user) {
        UserDto userDto = mapper.map(user, UserDto.class);
        UserDto updatedUser = userService.updateUser(userId, userDto);
        return ResponseEntity.ok(mapper.map(updatedUser, ResponseUser.class));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
