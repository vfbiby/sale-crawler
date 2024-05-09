package com.muhuang.salecrawler;

import com.muhuang.salecrawler.item.Item;
import com.muhuang.salecrawler.item.ItemRepository;
import com.muhuang.salecrawler.item.ItemService;
import com.muhuang.salecrawler.shop.ShopService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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
public class ItemControllerTest {

    @Resource
    private ItemRepository itemRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Resource
    private ItemService itemService;

    @BeforeEach
    public void cleanup() {
        itemRepository.deleteAll();
    }

    @Nested
    class HappyPath {

        @Test
        void postItem_whenItemIsValid_receiveOK() {
            ResponseEntity<Object> response = postItem(createValidItem(), Object.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @Test
        void postItem_whenItemIsValid_receiveSuccessMessage() {
            ResponseEntity<Object> response = postItem(createValidItem(), Object.class);
            assertThat(response.getBody()).isNotNull();
        }

        @Test
        void postItem_whenItemIsValid_ItemSaveToDatabase() {
            postItem(createValidItem(), Object.class);
            assertThat(itemRepository.count()).isEqualTo(1);
        }

        @Test
        void postItem_whenItemIsValid_ItemSaveToDatabaseWithPublishedAt() {
            itemService.save(createValidItem());
            Item inDB = itemRepository.findAll().get(0);
            assertThat(inDB.getPublishedAt()).isNotNull();
        }

    }

    private <T> ResponseEntity<T> postItem(Item item, Class<T> responseType) {
        return testRestTemplate.postForEntity("/api/1.0/items", item, responseType);
    }

    private static Item createValidItem() {
        return Item.builder().itemId("32838242344").name("气质连衣裙").build();
    }

}
