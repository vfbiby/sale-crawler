package com.muhuang.salecrawler.login;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/1.0/login")
public class LoginController {

    @PostMapping
    void login() {

    }

}
