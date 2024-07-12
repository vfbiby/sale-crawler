package com.muhuang.salecrawler.user;

import com.muhuang.salecrawler.share.TestUtil;
import com.muhuang.salecrawler.shared.ApiError;
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
        authenticate();
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
        authenticate();
        ResponseEntity<Object> response = login(Object.class);
        assertThat(response.getHeaders().containsKey("www-authenticate")).isFalse();
    }

    @Test
    void postLogin_withValidCredentials_receiveOK() {
        userService.save(TestUtil.createValidUser());
        authenticate();
        ResponseEntity<String> response = testRestTemplate.postForEntity(API_1_0_LOGIN, TestUtil.createValidUser(), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void postLogin_withValidCredentials_receiveLoggedInUserId() {
        User inDB = userService.save(TestUtil.createValidUser());
        authenticate();
        ResponseEntity<UserVM> response = login(new ParameterizedTypeReference<>() {
        });
        UserVM body = response.getBody();
        assertThat(body.getId()).isEqualTo(inDB.getId());
    }

    @Test
    void postLogin_withValidCredentials_receiveLoggedInUserImage() {
        User inDB = userService.save(TestUtil.createValidUser());
        authenticate();
        ResponseEntity<Map<String, Object>> response = login(new ParameterizedTypeReference<>() {
        });
        Map<String, Object> body = response.getBody();
        String image = (String) Objects.requireNonNull(body).get("image");
        assertThat(image).isEqualTo(inDB.getImage());
    }

    @Test
    void postLogin_withValidCredentials_receiveLoggedInUserDisplayName() {
        User inDB = userService.save(TestUtil.createValidUser());
        authenticate();
        ResponseEntity<Map<String, Object>> response = login(new ParameterizedTypeReference<>() {
        });
        Map<String, Object> body = response.getBody();
        String displayName = (String) Objects.requireNonNull(body).get("displayName");
        assertThat(displayName).isEqualTo(inDB.getDisplayName());
    }

    @Test
    void postLogin_withValidCredentials_receiveLoggedInUserUsername() {
        User inDB = userService.save(TestUtil.createValidUser());
        authenticate();
        ResponseEntity<Map<String, Object>> response = login(new ParameterizedTypeReference<>() {
        });
        Map<String, Object> body = response.getBody();
        String username = (String) Objects.requireNonNull(body).get("username");
        assertThat(username).isEqualTo(inDB.getUsername());
    }

    @Test
    void postLogin_withValidCredentials_notReceiveLoggedInUserPassword() {
        userService.save(TestUtil.createValidUser());
        authenticate();
        ResponseEntity<Map<String, Object>> response = login(new ParameterizedTypeReference<>() {
        });
        Map<String, Object> body = response.getBody();
        assertThat(Objects.requireNonNull(body).containsKey("password")).isFalse();
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

    private void authenticate() {
        testRestTemplate.getRestTemplate().getInterceptors()
                .add(new BasicAuthenticationInterceptor("test-username", "P4sword"));
    }

}
