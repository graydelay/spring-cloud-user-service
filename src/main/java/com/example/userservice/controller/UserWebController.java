package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserWebController {
    private final UserService userService;
    private final ModelMapper mapper;

    public UserWebController(UserService userService) {
        this.userService = userService;
        this.mapper = new ModelMapper();
    }

    @GetMapping("/list")
    public String list(@PageableDefault(size = 10) Pageable pageable, Model model) {
        Page<UserEntity> users = userService.getUserByAll(pageable);
        model.addAttribute("users", users);
        return "users/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "users/edit";
    }

    @GetMapping("/edit/{userId}")
    public String editForm(@PathVariable String userId, Model model) {
        UserDto user = userService.getUserByUserId(userId);
        model.addAttribute("user", user);
        return "users/edit";
    }
} 