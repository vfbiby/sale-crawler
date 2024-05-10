package com.muhuang.salecrawler;

import com.muhuang.salecrawler.item.Item;
import com.muhuang.salecrawler.item.ItemRepository;
import com.muhuang.salecrawler.item.ItemService;
import com.muhuang.salecrawler.shop.Shop;
import com.muhuang.salecrawler.shop.ShopService;
import jakarta.annotation.Resource;
import lombok.Builder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.muhuang.salecrawler.ShopControllerTest.createValidShop;
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

    @Resource
    private ShopService shopService;

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

        @Builder
        record ShopDTO(String itemId, String name, Shop shop) {
        }

        @Test
        void postItem_whenItemIsValid_ItemSaveToDatabaseWithShopInfo() {
            Shop savedShop = shopService.save(createValidShop());
            ShopDTO shopDTO = ShopDTO.builder().itemId("3244282383").name("2024气质新款连衣裙").shop(savedShop).build();
            testRestTemplate.postForEntity("/api/1.0/items", shopDTO, Object.class);
            Item inDB = itemRepository.findAll().get(0);
            assertThat(inDB.getShop().getId()).isNotNull();
        }

    }

    @Nested
    class SadPath {

        @Test
        void postItem_whenItemHasNullItemId_receiveBadRequest() {
            Item item = createValidItem();
            item.setItemId(null);
            ResponseEntity<Object> response = postItem(item, Object.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        void postItem_whenItemIdLessThan10Characters_receiveBadRequest() {
            Item item = createValidItem();
            item.setItemId("123456789");
            ResponseEntity<Object> response = postItem(item, Object.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        void postItem_whenItemIdIs30Characters_receiveOK() {
            Item item = createValidItem();
            String longString = IntStream.rangeClosed(1, 30).mapToObj(i -> "x").collect(Collectors.joining());
            item.setItemId(longString);
            ResponseEntity<Object> response = postItem(item, Object.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @Test
        void postItem_whenItemIdIsMoreThan30Characters_receiveBadRequest() {
            Item item = createValidItem();
            String longString = IntStream.rangeClosed(1, 31).mapToObj(i -> "x").collect(Collectors.joining());
            item.setItemId(longString);
            ResponseEntity<Object> response = postItem(item, Object.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        void postItem_whenItemNameLessThan10Characters_receiveBadRequest() {
            Item item = createValidItem();
            item.setName("短名字");
            ResponseEntity<Object> response = postItem(item, Object.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        void postItem_whenItemNameIs60Characters_receiveOK() {
            Item item = createValidItem();
            String longName = IntStream.rangeClosed(1, 60).mapToObj(i -> "x").collect(Collectors.joining());
            item.setName(longName);
            ResponseEntity<Object> response = postItem(item, Object.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @Test
        void postItem_whenItemNameIsMoreThan60Characters_receiveBadRequest() {
            Item item = createValidItem();
            String longName = IntStream.rangeClosed(1, 61).mapToObj(i -> "x").collect(Collectors.joining());
            item.setName(longName);
            ResponseEntity<Object> response = postItem(item, Object.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        void postItem_whenItemHasNullItemName_receiveBadRequest() {
            Item item = createValidItem();
            item.setName(null);
            ResponseEntity<Object> response = postItem(item, Object.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

    }

    private <T> ResponseEntity<T> postItem(Item item, Class<T> responseType) {
        return testRestTemplate.postForEntity("/api/1.0/items", item, responseType);
    }

    private static Item createValidItem() {
        return Item.builder().itemId("32838242344").name("2024气质新款连衣裙").build();
    }

}
