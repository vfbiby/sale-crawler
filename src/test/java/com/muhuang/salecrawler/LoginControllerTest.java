package com.muhuang.salecrawler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class LoginControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @AfterEach
    public void cleanup() {
        unauthenticate();
    }

    @Test
    void postLogin_withoutUserCredentials_receiveUnauthorized() {
        ResponseEntity<Object> response = testRestTemplate.postForEntity("/api/1.0/login", null, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void postLogin_withIncorrectCredentials_receiveUnauthorized() {
        authenticate();
        ResponseEntity<Object> response = testRestTemplate.postForEntity("/api/1.0/login", null, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    private void unauthenticate() {
        testRestTemplate.getRestTemplate().getInterceptors().clear();
    }

    private void authenticate() {
        testRestTemplate.getRestTemplate().getInterceptors()
                .add(new BasicAuthenticationInterceptor("test-user", "P4ssword"));
    }

}
