package com.muhuang.salecrawler.user;

import com.muhuang.salecrawler.share.TestUtil;
import com.muhuang.salecrawler.user.User;
import com.muhuang.salecrawler.user.UserRepository;
import com.muhuang.salecrawler.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void should_hashed_password_when_save_user() {
        User validUser = TestUtil.createValidUser();
        String password = validUser.getPassword();
        new UserService(userRepository, passwordEncoder).save(validUser);
        verify(passwordEncoder, times(1)).encode(password);
    }

}
