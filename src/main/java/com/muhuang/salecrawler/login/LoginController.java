package com.muhuang.salecrawler.login;

import com.fasterxml.jackson.annotation.JsonView;
import com.muhuang.salecrawler.configuration.AuthServices;
import com.muhuang.salecrawler.user.User;
import com.muhuang.salecrawler.user.UserVM;
import com.muhuang.salecrawler.user.Views;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/1.0/login")
public class LoginController {

    private final AuthServices authService;

    public LoginController(AuthServices authService) {
        this.authService = authService;
    }

    @PostMapping
    UserVM handleLogin(Authentication authentication) {
        UserDetails userDetails = authService.loadUserByUsername(authentication.getName());
        return new UserVM((User) userDetails);
    }

}
