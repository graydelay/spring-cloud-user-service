package com.example.userservice.controller;

import com.example.userservice.jpa.UserEntity;
import com.example.userservice.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserViewController {
    private final UserService userService;

    public UserViewController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public String listUsers(@PageableDefault(size = 10) Pageable pageable, Model model) {
        Page<UserEntity> users = userService.getUserByAll(pageable);
        model.addAttribute("users", users);
        return "users/list";
    }
} 