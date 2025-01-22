package com.example.userservice.controller;

import com.example.userservice.vo.Greeting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("/")
public class HomeController {
    private final Environment env;
    private final Greeting greeting;

    public HomeController(Environment env, Greeting greeting) {
        this.env = env;
        this.greeting = greeting;
    }

    @GetMapping("health_check")
    @ResponseBody
    public String status() {
        return "It's Working in User Service"
                + ", port(local.server.port)=" + env.getProperty("local.server.port")
                + ", port(server.port)=" + env.getProperty("server.port")
                + ", token secret=" + env.getProperty("token.secret")
                + ", token expiration time=" + env.getProperty("token.expiration_time");
    }

    @GetMapping("welcome")
    @ResponseBody
    public String welcome() {
        return greeting.getMessage();
    }

    @GetMapping
    public String home() {
        return "home"; // Return Thymeleaf template named 'home'
    }
}
