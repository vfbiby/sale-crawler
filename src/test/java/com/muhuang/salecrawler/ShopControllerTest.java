package com.muhuang.salecrawler;

import com.muhuang.salecrawler.shared.ApiError;
import com.muhuang.salecrawler.shared.GenericResponse;
import com.muhuang.salecrawler.shop.Shop;
import com.muhuang.salecrawler.shop.ShopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ShopControllerTest {

    public static final String API_1_0_SHOP = "/api/1.0/shops";

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    public void cleanup() {
        shopRepository.deleteAll();
    }

    @Nested
    class HappyPath {

        @Test
        void postShop_whenShopIsValid_receiveOK() {
            Shop shop = TestUtil.createValidShop();
            ResponseEntity<Object> response = postShop(shop, Object.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @Test
        void postShop_whenShopIsValid_saveToDatabase() {
            postShop(TestUtil.createValidShop(), Object.class);
            assertThat(shopRepository.count()).isEqualTo(1);
        }

        @Test
        void postShop_whenShopIsValid_receiveSuccessMessage() {
            Shop shop = TestUtil.createValidShop();
            ResponseEntity<GenericResponse> response = postShop(shop, GenericResponse.class);
            assertThat(Objects.requireNonNull(response.getBody()).getMessage()).isNotNull();
        }

    }

    @Nested
    class SadPath {

        @Test
        void postShop_whenShopHasNullShopId_receiveBadRequest() {
            Shop shop = TestUtil.createValidShop();
            shop.setOutShopId(null);
            ResponseEntity<GenericResponse> response = postShop(shop, GenericResponse.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        void postShop_whenShopHasNullShopName_receiveBadRequest() {
            Shop shop = TestUtil.createValidShop();
            shop.setShopName(null);
            ResponseEntity<GenericResponse> response = postShop(shop, GenericResponse.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        void postShop_whenShopHasNullShopUrl_receiveBadRequest() {
            Shop shop = TestUtil.createValidShop();
            shop.setShopUrl(null);
            ResponseEntity<GenericResponse> response = postShop(shop, GenericResponse.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        void postShop_whenShopHasInvalidShopUrl_receiveBadRequest() {
            Shop shop = TestUtil.createValidShop();
            shop.setShopUrl("not-valid-url");
            ResponseEntity<GenericResponse> response = postShop(shop, GenericResponse.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

    }

    @Nested
    class Read {

        @Test
        void getShops_whenThereIsNoShopsInDB_receiveOK() {
            ResponseEntity<Object> response = getShops(new ParameterizedTypeReference<>() {
            });
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @Test
        void getShops_whenThereIsNoShopsInDB_receivePageWithZeroItems() {
            ResponseEntity<TestPage<Object>> response = getShops(new ParameterizedTypeReference<>() {
            });
            assertThat(Objects.requireNonNull(response.getBody()).getTotalElements()).isEqualTo(0);
        }

        @Test
        void getShops_whenThereIsAShopsInDB_receivePageWithUser() {
            shopRepository.save(TestUtil.createValidShop());
            ResponseEntity<TestPage<Object>> response = getShops(new ParameterizedTypeReference<>() {
            });
            assertThat(Objects.requireNonNull(response.getBody()).getTotalElements()).isEqualTo(1);
        }

        private <T> ResponseEntity<T> getShops(ParameterizedTypeReference<T> responseType) {
            return testRestTemplate.exchange(API_1_0_SHOP, HttpMethod.GET, null, responseType);
        }

    }

    @Nested
    class Create {

        @Nested
        class SadPath {

            @Test
            void postShop_whenAnotherShopHasSameOutShopId_receiveBadRequest() {
                Shop validShop = TestUtil.createValidShop();
                shopRepository.save(validShop);
                ResponseEntity<Object> response = postShop(validShop, Object.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            }

            @Test
            void postShop_whenAnotherShopHasSameOutShopId_receiveDuplicateShopId() {
                Shop validShop = TestUtil.createValidShop();
                shopRepository.save(validShop);
                ResponseEntity<ApiError> response = postShop(validShop, ApiError.class);
                Map<String, String> validationErrors = Objects.requireNonNull(response.getBody()).getValidationErrors();
                assertThat(validationErrors.get("outShopId")).isEqualTo("This shop is in database");
            }

        }
    }

    private <T> ResponseEntity<T> postShop(Shop shop, Class<T> responseType) {
        return testRestTemplate.postForEntity(API_1_0_SHOP, shop, responseType);
    }

}
