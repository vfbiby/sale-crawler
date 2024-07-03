package com.muhuang.salecrawler;

import com.muhuang.salecrawler.cate.CateRepository;
import com.muhuang.salecrawler.item.Item;
import com.muhuang.salecrawler.item.ItemRepository;
import com.muhuang.salecrawler.item.ItemService;
import com.muhuang.salecrawler.shop.Shop;
import com.muhuang.salecrawler.shop.ShopRepository;
import com.muhuang.salecrawler.shop.ShopService;
import jakarta.annotation.Resource;
import org.h2.util.TempFileDeleter;
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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @Resource
    private ShopRepository shopRepository;

    @Resource
    private CateRepository cateRepository;

    @BeforeEach
    public void cleanup() {
        itemRepository.deleteAll();
        cateRepository.deleteAll();
        shopRepository.deleteAll();
    }

    @Nested
    class HappyPath {

        @Test
        void postItem_whenItemIsValid_receiveOK() {
            ResponseEntity<Object> response = postItem(createValidItemWithShop(), Object.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @Test
        void postItem_whenItemIsValid_receiveSuccessMessage() {
            ResponseEntity<Object> response = postItem(TestUtil.createValidItem(), Object.class);
            assertThat(response.getBody()).isNotNull();
        }

        @Test
        void postItem_whenItemIsValid_ItemSaveToDatabase() {
            postItem(createValidItemWithShop(), Object.class);
            assertThat(itemRepository.count()).isEqualTo(1);
        }

        @Test
        void postItem_whenItemIsValid_ItemSaveToDatabaseWithPublishedAt() {
            itemService.save(createValidItemWithShop());
            Item inDB = itemRepository.findAll().get(0);
            assertThat(inDB.getPublishedAt()).isNotNull();
        }

        @Test
        void postItem_whenItemIsValid_ItemSaveToDatabaseWithShopInfo() {
            Shop savedShop = shopService.save(createValidShop());
            Item shop = Item.builder().outItemId("3244282383").title("2024气质新款连衣裙").shop(savedShop).build();
            postItem(shop, Object.class);
            Item inDB = itemRepository.findAll().get(0);
            assertThat(inDB.getShop().getId()).isNotNull();
        }

    }

    private static Shop createValidShop() {
        return Shop.builder().outShopId("38888273").shopName("SKY").shopUrl("https://sky.taobao.com").build();
    }

    @Nested
    class SadPath {

        @Test
        void postItem_whenItemHasNullItemId_receiveBadRequest() {
            Item item = TestUtil.createValidItem();
            item.setOutItemId(null);
            ResponseEntity<Object> response = postItem(item, Object.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        void postItem_whenItemIdLessThan10Characters_receiveBadRequest() {
            Item item = TestUtil.createValidItem();
            item.setOutItemId("123456789");
            ResponseEntity<Object> response = postItem(item, Object.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        void postItem_whenItemIdIs30Characters_receiveOK() {
            Item item = createValidItemWithShop();
            String longString = IntStream.rangeClosed(1, 30).mapToObj(i -> "x").collect(Collectors.joining());
            item.setOutItemId(longString);
            ResponseEntity<Object> response = postItem(item, Object.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @Test
        void postItem_whenItemIdIsMoreThan30Characters_receiveBadRequest() {
            Item item = TestUtil.createValidItem();
            String longString = IntStream.rangeClosed(1, 31).mapToObj(i -> "x").collect(Collectors.joining());
            item.setOutItemId(longString);
            ResponseEntity<Object> response = postItem(item, Object.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        void postItem_whenItemNameLessThan10Characters_receiveBadRequest() {
            Item item = TestUtil.createValidItem();
            item.setTitle("短名字");
            ResponseEntity<Object> response = postItem(item, Object.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        void postItem_whenItemNameIs60Characters_receiveOK() {
            Item item = createValidItemWithShop();
            String longName = IntStream.rangeClosed(1, 60).mapToObj(i -> "x").collect(Collectors.joining());
            item.setTitle(longName);
            ResponseEntity<Object> response = postItem(item, Object.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @Test
        void postItem_whenItemNameIsMoreThan60Characters_receiveBadRequest() {
            Item item = TestUtil.createValidItem();
            String longName = IntStream.rangeClosed(1, 61).mapToObj(i -> "x").collect(Collectors.joining());
            item.setTitle(longName);
            ResponseEntity<Object> response = postItem(item, Object.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        void postItem_whenItemHasNullItemName_receiveBadRequest() {
            Item item = TestUtil.createValidItem();
            item.setTitle(null);
            ResponseEntity<Object> response = postItem(item, Object.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        void postItem_whenItemHasNoOutShopId_receiveBadRequest() {
            Item item = TestUtil.createValidItem();
            item.setShop(null);
            ResponseEntity<Object> response = postItem(item, Object.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

    }

    @Nested
    class Read {

        @Test
        void getItems_whenThereAreNoItemsInDB_receiveOK() {
            ResponseEntity<Object> response = testRestTemplate.getForEntity("/api/1.0/items", Object.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @Test
        void getItems_whenThereAreNoItemsInDB_receivePageWithZeroItems() {
            ResponseEntity<TestPage<Object>> response = getItems(new ParameterizedTypeReference<>() {
            });
            assertThat(Objects.requireNonNull(response.getBody()).getTotalElements()).isEqualTo(0);
        }

        @Test
        void getItems_whenThereIsAItemInDB_receivePageWithItem() {
            itemRepository.save(createValidItemWithShop());
            ResponseEntity<TestPage<Object>> response = getItems(new ParameterizedTypeReference<>() {
            });
            assertThat(Objects.requireNonNull(response.getBody()).getTotalElements()).isEqualTo(1);
        }

        @Test
        void getItems_whenThereAreTwoItemInDB_defaultSortByIdDESC() {
            String itemIdTwo = "32838242345";
            createOneShopAndTwoItem("32838242344", itemIdTwo);
            ResponseEntity<TestPage<Item>> response = getItems("/api/1.0/items");
            List<Item> items = Objects.requireNonNull(response.getBody()).getContent();
            assertThat(items.get(0).getOutItemId()).isEqualTo(itemIdTwo);
        }

        @Test
        void getItems_whenThereAreTwoItemInDBAndSortByPublishedAt_defaultDirectionIsDesc() {
            String itemIdTwo = "32838242345";
            createOneShopAndTwoItem("32838242344", itemIdTwo);
            ResponseEntity<TestPage<Item>> response = getItems("/api/1.0/items?sortBy=publishedAt");
            List<Item> items = Objects.requireNonNull(response.getBody()).getContent();
            assertThat(items.get(0).getOutItemId()).isEqualTo(itemIdTwo);
        }

        @Test
        void getItems_whenThereAreTwoItemInDBAndSortByPublishedAtASC_olderItemAtFirst() {
            String itemIdOne = "32838242344";
            createOneShopAndTwoItem(itemIdOne, "32838242345");
            ResponseEntity<TestPage<Item>> response = getItems("/api/1.0/items?sortBy=publishedAt&direction=ASC");
            List<Item> items = Objects.requireNonNull(response.getBody()).getContent();
            assertThat(items.get(0).getOutItemId()).isEqualTo(itemIdOne);
        }

        private void createOneShopAndTwoItem(String itemIdOne, String itemIdTwo) {
            Shop shopInDB = shopRepository.save(createValidShop());
            Item item1 = createValidItemWithDate(itemIdOne, "2022-06-25");
            Item item2 = createValidItemWithDate(itemIdTwo, "2023-06-25");
            item1.setShop(shopInDB);
            item2.setShop(shopInDB);
            itemRepository.saveAll(List.of(item1, item2));
        }

    }

    private static Item createValidItemWithDate(String itemId, String date) {
        return TestUtil.createItemWithDetail(itemId, date, "2024气质新款连衣裙", "https://x.taobao.com/v.img");
    }

    private ResponseEntity<TestPage<Item>> getItems(String url) {
        return getItems(url, new ParameterizedTypeReference<>() {
        });
    }

    private ResponseEntity<TestPage<Object>> getItems(ParameterizedTypeReference<TestPage<Object>> responseType) {
        return getItems("/api/1.0/items", responseType);
    }

    private <T> ResponseEntity<TestPage<T>> getItems(String url, ParameterizedTypeReference<TestPage<T>> responseType) {
        return testRestTemplate.exchange(url, HttpMethod.GET, null, responseType);
    }

    private <T> ResponseEntity<T> postItem(Item item, Class<T> responseType) {
        return testRestTemplate.postForEntity("/api/1.0/items", item, responseType);
    }

    public Item createValidItemWithShop() {
        Shop validShop = createValidShop();
        shopRepository.save(validShop);
        Item item = TestUtil.createValidItem();
        item.setShop(validShop);
        return item;
    }

}
