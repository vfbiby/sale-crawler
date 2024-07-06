package com.muhuang.salecrawler;

import com.muhuang.salecrawler.item.entity.Item;
import com.muhuang.salecrawler.item.repository.ItemRepository;
import com.muhuang.salecrawler.item.repository.ItemSalesScrapingScheduleRepository;
import com.muhuang.salecrawler.shared.ApiError;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Frank An
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ItemSalesScrapingScheduleControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ItemSalesScrapingScheduleRepository itemSalesScrapingScheduleRepository;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        Item item = TestUtil.createValidItem();
        itemRepository.save(item);
    }

    @AfterEach
    void tearDown() {
        itemSalesScrapingScheduleRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    @DisplayName("当itemId为null时，HttpStatus.BAD_REQUEST")
    void postSchedule_whenItemHasNullId_receiveBadRequest() {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("itemId", null);

        ResponseEntity<Object> response = testRestTemplate
                .postForEntity("/api/1.0/items/{itemId}/schedule", null, Object.class, uriVariables);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("当itemId不为null时，HttpStatus.OK")
    void postSchedule_whenItemHasId_receiveOK() {
        ResponseEntity<Object> response = testRestTemplate
                .postForEntity("/api/1.0/items/32838242344/schedule", null, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    @DisplayName("当商品存在时，创建该商品的销量抓取计划并保存到数据库")
    void postSchedule_whenItemIsValid_saveScheduleToDatabase() {
        testRestTemplate.postForEntity("/api/1.0/items/32838242344/schedule", null, Object.class);
        assertThat(itemSalesScrapingScheduleRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("当商品不存在时， HttpStatus.BAD_REQUEST")
    void postSchedule_whenItemIsInvalid_receiveBadRequest() {
        ResponseEntity<ApiError> response = testRestTemplate
                .postForEntity("/api/1.0/items/1/schedule", null, ApiError.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("当商品不存在时，返回消息商品不存在")
    void postSchedule_whenItemIsInvalid_receiveMessageOfItemNotFound() {
        ResponseEntity<ApiError> response = testRestTemplate
                .postForEntity("/api/1.0/items/1/schedule", null, ApiError.class);
        assertThat(response.getBody()).isNotNull();
        System.out.println(response.getBody());
        assertThat(response.getBody().getMessage()).isEqualTo("商品不存在");
    }


}
