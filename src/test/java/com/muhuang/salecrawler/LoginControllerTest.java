package com.muhuang.salecrawler;

import com.muhuang.salecrawler.shared.ApiError;
import com.muhuang.salecrawler.user.User;
import com.muhuang.salecrawler.user.UserRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.test.context.ActiveProfiles;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class LoginControllerTest {

    public static final String API_1_0_LOGIN = "/api/1.0/login";
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Resource
    private UserRepository userRepository;

    @AfterEach
    public void cleanup() {
        unauthenticated();
//        userRepository.deleteAll();
    }

    @Test
    void postLogin_withoutUserCredentials_receiveUnauthorized() {
        ResponseEntity<Object> response = testRestTemplate.postForEntity(API_1_0_LOGIN, null, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void postLogin_withIncorrectCredentials_receiveUnauthorized() {
        authenticate();
        ResponseEntity<Object> response = testRestTemplate.postForEntity(API_1_0_LOGIN, null, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void postLogin_withoutUserCredentials_receiveApiError() {
        ResponseEntity<ApiError> response = testRestTemplate.postForEntity(API_1_0_LOGIN, null, ApiError.class);
        assertThat(Objects.requireNonNull(response.getBody()).getUrl()).isEqualTo(API_1_0_LOGIN);
    }

    @Test
    void postLogin_withoutUserCredentials_receiveApiErrorWithoutValidationErrors() {
        ResponseEntity<String> response = testRestTemplate.postForEntity(API_1_0_LOGIN, null, String.class);
        assertThat(Objects.requireNonNull(response.getBody()).contains("validationErrors")).isFalse();
    }

    @Test
    void postLogin_withIncorrectCredentials_receiveUnauthorizedWithoutWWWAuthenticationHeader() {
        authenticate();
        ResponseEntity<Object> response = testRestTemplate.postForEntity(API_1_0_LOGIN, null, Object.class);
        assertThat(response.getHeaders().containsKey("www-authenticate")).isFalse();
    }

    @Test
    void postLogin_withValidCredentials_receiveOK() {
        User user = new User();
        user.setDisplayName("test-display");
        user.setUsername("test-username");
        user.setPassword("P4sword");
        ResponseEntity<String> response = testRestTemplate.postForEntity(API_1_0_LOGIN, user, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private void unauthenticated() {
        testRestTemplate.getRestTemplate().getInterceptors().clear();
    }

    private void authenticate() {
        testRestTemplate.getRestTemplate().getInterceptors()
                .add(new BasicAuthenticationInterceptor("test-user", "P4ssword"));
    }

}
