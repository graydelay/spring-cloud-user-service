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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/user-service")
public class UserController {
    private final Environment env;
    private final Greeting greeting;
    private final UserService userService;

    public UserController(Environment env, Greeting greeting, UserService userService) {
        this.env = env;
        this.greeting = greeting;
        this.userService = userService;
    }

    @GetMapping("/health_check")
    public String status() {
        return String.format("It's Working in User Service on PORT %s", env.getProperty("local.server.port"));
    }

    @GetMapping("/welcome")
    public String welcome() {
//        return env.getProperty("greeting.message");
        return greeting.getMessage();
    }

    @PostMapping("/users")
    public ResponseEntity createUser(@RequestBody RequestUser user) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = mapper.map(user, UserDto.class);
        UserDto response = userService.createUser(userDto);

        ResponseUser responseUser = mapper.map(response, ResponseUser.class);
        return new ResponseEntity(responseUser, HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public ResponseEntity getUsers() {
        Iterable<UserEntity> userByAll = userService.getUserByAll();
        ArrayList<ResponseUser> result = new ArrayList<>();
        userByAll.forEach(user ->
                result.add(new ModelMapper().map(user, ResponseUser.class))
        );

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity getUser(@PathVariable("userId") String userId) {
        UserDto userByUserId = userService.getUserByUserId(userId);
        ResponseUser result = new ModelMapper().map(userByUserId, ResponseUser.class);

        return new ResponseEntity(result, HttpStatus.OK);
    }
}
