package com.muhuang.salecrawler;

import com.muhuang.salecrawler.user.User;

public class TestUtil {
    static User createValidUser() {
        User user = new User();
        user.setDisplayName("test-display");
        user.setUsername("test-username");
        user.setPassword("P4sword");
        return user;
    }
}
