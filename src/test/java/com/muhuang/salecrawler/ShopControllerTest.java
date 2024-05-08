package com.muhuang.salecrawler;

import com.muhuang.salecrawler.shop.Shop;
import com.muhuang.salecrawler.shop.ShopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
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

    @Test
    void postShop_whenShopIsValid_receiveOK() {
        Shop shop = createValidShop();
        ResponseEntity<Object> response = testRestTemplate.postForEntity(API_1_0_SHOP, shop, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void postShop_whenShopIsValid_saveToDatabase() {
        Shop shop = createValidShop();
        testRestTemplate.postForEntity(API_1_0_SHOP, shop, Object.class);
        assertThat(shopRepository.count()).isEqualTo(1);
    }

    private static Shop createValidShop() {
        return Shop.builder().shopId(38888273L).shopName("SKY").shopUrl("https://sky.taobao.com").build();
    }

}
