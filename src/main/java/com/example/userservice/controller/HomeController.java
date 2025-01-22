package com.example.userservice.controller;

import com.example.userservice.vo.Greeting;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    private final Environment env;
    private final Greeting greeting;

    public HomeController(Environment env, Greeting greeting) {
        this.env = env;
        this.greeting = greeting;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("greeting", "안녕하세요! Spring Cloud User Service 입니다.");
        return "home";
    }

    @GetMapping("/health_check")
    @ResponseBody
    public String status() {
        return "It's Working in User Service"
                + ", port(local.server.port)=" + env.getProperty("local.server.port")
                + ", port(server.port)=" + env.getProperty("server.port")
                + ", token secret=" + env.getProperty("token.secret")
                + ", token expiration time=" + env.getProperty("token.expiration_time");
    }

    @GetMapping("/welcome")
    @ResponseBody
    public String welcome() {
        return greeting.getMessage();
    }
} 