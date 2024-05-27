package com.muhuang.salecrawler.login;

import com.muhuang.salecrawler.configuration.AuthServices;
import com.muhuang.salecrawler.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/1.0/login")
public class LoginController {

    private final AuthServices authService;

    public LoginController(AuthServices authService) {
        this.authService = authService;
    }

    @PostMapping
    Map<String, Object> handleLogin(Authentication authentication) {
        User user = ((User) authService.loadUserByUsername(authentication.getName()));
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("image", user.getImage());
        return userMap;
    }

}
