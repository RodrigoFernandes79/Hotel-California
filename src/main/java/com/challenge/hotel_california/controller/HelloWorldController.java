package com.challenge.hotel_california.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping("/hello")
    public String getHello() {
        return "Hello World!🌎";
    }
}
