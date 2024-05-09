package com.muhuang.salecrawler;

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
            Shop shop = createValidShop();
            ResponseEntity<Object> response = postForEntity(shop, Object.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @Test
        void postShop_whenShopIsValid_saveToDatabase() {
            postForEntity(createValidShop(), Object.class);
            assertThat(shopRepository.count()).isEqualTo(1);
        }

        @Test
        void postShop_whenShopIsValid_receiveSuccessMessage() {
            Shop shop = createValidShop();
            ResponseEntity<GenericResponse> response = postForEntity(shop, GenericResponse.class);
            assertThat(response.getBody().getMessage()).isNotNull();
        }

    }

    @Nested
    class SadPath {

        @Test
        void postShop_whenShopHasNullShopId_receiveBadRequest() {
            Shop shop = createValidShop();
            shop.setShopId(null);
            ResponseEntity<GenericResponse> response = postForEntity(shop, GenericResponse.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        void postShop_whenShopHasNullShopName_receiveBadRequest() {
            Shop shop = createValidShop();
            shop.setShopName(null);
            ResponseEntity<GenericResponse> response = postForEntity(shop, GenericResponse.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        void postShop_whenShopHasNullShopUrl_receiveBadRequest() {
            Shop shop = createValidShop();
            shop.setShopUrl(null);
            ResponseEntity<GenericResponse> response = postForEntity(shop, GenericResponse.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        void postShop_whenShopHasInvalidShopUrl_receiveBadRequest() {
            Shop shop = createValidShop();
            shop.setShopUrl("not-valid-url");
            ResponseEntity<GenericResponse> response = postForEntity(shop, GenericResponse.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

    }

    @Nested
    class ReadShop {

        @Test
        void getShops_whenThereIsNoShopsInDB_receiveOK() {
            ResponseEntity<Object> response = getShops(new ParameterizedTypeReference<>() {});
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @Test
        void getShops_whenThereIsNoShopsInDB_receivePageWithZeroItems() {
            ResponseEntity<TestPage<Object>> response = getShops(new ParameterizedTypeReference<>() {});
            assertThat(response.getBody().getTotalElements()).isEqualTo(0);
        }

        @Test
        void getShops_whenThereIsAShopsInDB_receivePageWithUser() {
            shopRepository.save(createValidShop());
            ResponseEntity<TestPage<Object>> response = getShops(new ParameterizedTypeReference<>() {});
            System.out.println(response);
            assertThat(response.getBody().getTotalElements()).isEqualTo(1);
        }

        private <T> ResponseEntity<T> getShops(ParameterizedTypeReference<T> responseType) {
            return testRestTemplate.exchange(API_1_0_SHOP, HttpMethod.GET, null, responseType);
        }

    }

    private <T> ResponseEntity<T> postForEntity(Shop shop, Class<T> responseType) {
        return testRestTemplate.postForEntity(API_1_0_SHOP, shop, responseType);
    }

    private static Shop createValidShop() {
        return Shop.builder().shopId(38888273L).shopName("SKY").shopUrl("https://sky.taobao.com").build();
    }

}
