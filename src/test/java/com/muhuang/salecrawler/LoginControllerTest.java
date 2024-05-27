package com.muhuang.salecrawler;

import com.muhuang.salecrawler.shared.ApiError;
import com.muhuang.salecrawler.user.User;
import com.muhuang.salecrawler.user.UserRepository;
import com.muhuang.salecrawler.user.UserService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class LoginControllerTest {

    public static final String API_1_0_LOGIN = "/api/1.0/login";
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Resource
    private UserService userService;

    @Resource
    private UserRepository userRepository;

    @AfterEach
    public void cleanup() {
        unauthenticated();
        userRepository.deleteAll();
    }

    @Test
    void postLogin_withoutUserCredentials_receiveUnauthorized() {
        ResponseEntity<Object> response = login(Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void postLogin_withIncorrectCredentials_receiveUnauthorized() {
        authenticate("wrong-username");
        ResponseEntity<Object> response = login(Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void postLogin_withoutUserCredentials_receiveApiError() {
        ResponseEntity<ApiError> response = login(ApiError.class);
        assertThat(Objects.requireNonNull(response.getBody()).getUrl()).isEqualTo(API_1_0_LOGIN);
    }

    @Test
    void postLogin_withoutUserCredentials_receiveApiErrorWithoutValidationErrors() {
        ResponseEntity<String> response = login(String.class);
        assertThat(Objects.requireNonNull(response.getBody()).contains("validationErrors")).isFalse();
    }

    @Test
    void postLogin_withIncorrectCredentials_receiveUnauthorizedWithoutWWWAuthenticationHeader() {
        authenticate("wrong-username");
        ResponseEntity<Object> response = login(Object.class);
        assertThat(response.getHeaders().containsKey("www-authenticate")).isFalse();
    }

    @Test
    void postLogin_withValidCredentials_receiveOK() {
        User user = TestUtil.createValidUser();
        userService.save(user);
        authenticate(user.getUsername());
        ResponseEntity<String> response = testRestTemplate.postForEntity(API_1_0_LOGIN, user, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void postLogin_withValidCredentials_receiveLoggedInUserId() {
        User user = TestUtil.createValidUser();
        User inDB = userService.save(user);
        authenticate(user.getUsername());
        ResponseEntity<Map<String, Object>> response = login(new ParameterizedTypeReference<>() {
        });
        Map<String, Object> body = response.getBody();
        Integer id = (Integer) body.get("id");
        assertThat(id).isEqualTo(inDB.getId());
    }

    @Test
    void postLogin_withValidCredentials_receiveLoggedInUserImage() {
        User user = TestUtil.createValidUser();
        User inDB = userService.save(user);
        authenticate(user.getUsername());
        ResponseEntity<Map<String, Object>> response = login(new ParameterizedTypeReference<>() {
        });
        Map<String, Object> body = response.getBody();
        String image = (String) body.get("image");
        assertThat(image).isEqualTo(inDB.getImage());
    }

    private <T> ResponseEntity<T> login(Class<T> responseType) {
        return testRestTemplate.postForEntity(API_1_0_LOGIN, null, responseType);
    }

    private <T> ResponseEntity<T> login(ParameterizedTypeReference<T> responseType) {
        return testRestTemplate.exchange(API_1_0_LOGIN, HttpMethod.POST, null, responseType);
    }

    private void unauthenticated() {
        testRestTemplate.getRestTemplate().getInterceptors().clear();
    }

    private void authenticate(String username) {
        testRestTemplate.getRestTemplate().getInterceptors()
                .add(new BasicAuthenticationInterceptor(username, "P4sword"));
    }

}
