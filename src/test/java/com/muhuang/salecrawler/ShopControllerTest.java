package com.muhuang.salecrawler;

import com.muhuang.salecrawler.shop.Shop;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShopControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void postShop_whenShopIsValid_receiveOK() {
        Shop sky = Shop.builder().shopId(38888273L).name("SKY").url("https://sky.taobao.com").build();
        ResponseEntity<Object> response = testRestTemplate.postForEntity("/api/1.0/shop", sky, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
