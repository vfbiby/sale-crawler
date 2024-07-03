package com.muhuang.salecrawler;

import com.muhuang.salecrawler.cate.CateRepository;
import com.muhuang.salecrawler.item.Item;
import com.muhuang.salecrawler.item.ItemDTO;
import com.muhuang.salecrawler.item.ItemRepository;
import com.muhuang.salecrawler.item.PluginItemDTO;
import com.muhuang.salecrawler.shared.ApiError;
import com.muhuang.salecrawler.shop.Shop;
import com.muhuang.salecrawler.shop.ShopRepository;
import com.muhuang.salecrawler.shop.ShopService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PluginCreateItemControllerTest {

    public static final String API_1_0_PLUGIN_ITEMS = "/api/1.0/plugin-items";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Resource
    private ItemRepository itemRepository;

    @Resource
    private ShopService shopService;

    @Resource
    private ShopRepository shopRepository;

    @Resource
    private CateRepository cateRepository;

    @Nested
    class Create {

        @AfterEach
        public void cleanup() {
            itemRepository.deleteAll();
            shopRepository.deleteAll();
            cateRepository.deleteAll();
        }

        @Nested
        class HappyPath {

            @BeforeEach
            public void setup() {
                insertValidShop();
            }

            @Test
            void postItem_whenShopAndItemIsValid_receiveOK() {
                PluginItemDTO pItem = TestUtil.createValidPluginItem();
                ResponseEntity<Object> response = postPluginItem(pItem, Object.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            }

            @Test
            void postItem_whenShopAndItemIsValid_itemSaveToDatabase() {
                PluginItemDTO pItem = TestUtil.createValidPluginItem();
                postPluginItem(pItem, Object.class);
                assertThat(itemRepository.count()).isEqualTo(1);
            }

            @Test
            void postItem_whenShopAndItemIsValid_itemSaveToDatabaseWithOutShopId() {
                PluginItemDTO pItem = TestUtil.createValidPluginItem();
                postPluginItem(pItem, Item.class);
                String savedItemOutShopId = itemRepository.findAll().get(0).getShop().getOutShopId();
                assertThat(savedItemOutShopId).isEqualTo(pItem.getShopId());
            }

            @Test
            void postItem_whenShopAndItemIsValid_receiveSuccessMessage() {
                PluginItemDTO pItem = TestUtil.createValidPluginItem();
                ResponseEntity<Object> response = postPluginItem(pItem, Object.class);
                assertThat(response.getBody()).isNotNull();
            }

            @Test
            void postItem_whenPluginItemIsValid_ItemSaveToDatabaseWithPublishedAt() {
                PluginItemDTO pItem = TestUtil.createValidPluginItem();
                postPluginItem(pItem, Object.class);
                Item inDB = itemRepository.findAll().get(0);
                assertThat(inDB.getPublishedAt()).isNotNull();
            }

            @Test
            void postItem_whenPluginItemIsValid_itemNameSaveAsTitle() {
                PluginItemDTO pItem = TestUtil.createValidPluginItem();
                postPluginItem(pItem, Object.class);
                Item inDB = itemRepository.findAll().get(0);
                assertThat(inDB.getTitle()).isEqualTo(pItem.getItems().get(0).getName());
            }

            @Test
            void postItem_whenPluginItemIsValid_itemPicSaveToDatabase() {
                PluginItemDTO pItem = TestUtil.createValidPluginItem();
                postPluginItem(pItem, Object.class);
                Item inDB = itemRepository.findAll().get(0);
                assertThat(inDB.getPic()).isNotNull();
            }

            @Test
            void postItem_whenPluginItemHasPublishedAt_itemSaveToDatabaseWithSpecifyPublishedAt() {
                PluginItemDTO pItem = TestUtil.createValidPluginItem();
                LocalDate publishedAt = LocalDate.now();
                pItem.setPublishedAt(publishedAt);
                postPluginItem(pItem, Object.class);
                Item inDB = itemRepository.findAll().get(0);
                assertThat((inDB.getPublishedAt().getTime()))
                        .isEqualTo(Timestamp.valueOf(publishedAt.atStartOfDay()).getTime());
            }

        }

        private void insertValidShop() {
            PluginItemDTO pluginItemDTO = TestUtil.createValidPluginItemShop();
            Shop shop = Shop.builder().outShopId(pluginItemDTO.getShopId())
                    .shopName(pluginItemDTO.getShopName())
                    .shopUrl(pluginItemDTO.getShopUrl()).build();
            shopService.save(shop);
        }

        @Nested
        class SadPath {

            @BeforeEach
            public void insertShop() {
                insertValidShop();
            }

            @Test
            void postItem_whenShopHasNullShopId_receiveBadRequest() {
                PluginItemDTO pItem = TestUtil.createValidPluginItem();
                pItem.setShopId(null);
                ResponseEntity<Object> response = postPluginItem(pItem, Object.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            }

            @Test
            void postItem_whenShopHasNullItems_receiveBadRequest() {
                PluginItemDTO pItem = TestUtil.createValidPluginItem();
                pItem.setItems(null);
                ResponseEntity<Object> response = postPluginItem(pItem, Object.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            }

            @Test
            void postItem_whenShopIsValidButShopIsNotInDB_receiveBadRequest() {
                PluginItemDTO pItem = TestUtil.createValidPluginItem();
                pItem.setShopId("not-in-db-shop");
                ResponseEntity<Object> response = postPluginItem(pItem, Object.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            }

            @Test
            void postItem_whenShopIsInvalid_receiveApiError() {
                PluginItemDTO pItem = new PluginItemDTO();
                ResponseEntity<ApiError> response = postPluginItem(pItem, ApiError.class);
                assertThat(Objects.requireNonNull(response.getBody()).getUrl()).isEqualTo(API_1_0_PLUGIN_ITEMS);
            }

            @Test
            void postItem_whenShopIsInvalid_receiveApiErrorWithValidationErrors() {
                PluginItemDTO pItem = new PluginItemDTO();
                ResponseEntity<ApiError> response = postPluginItem(pItem, ApiError.class);
                assertThat(Objects.requireNonNull(response.getBody()).getValidationErrors().size()).isEqualTo(2);
            }

            @Test
            void postItem_whenShopHasNullShopId_receiveMessageOfNullErrorForShopId() {
                PluginItemDTO pItem = TestUtil.createValidPluginItem();
                pItem.setShopId(null);
                ResponseEntity<ApiError> response = postPluginItem(pItem, ApiError.class);
                Map<String, String> validationErrors = Objects.requireNonNull(response.getBody()).getValidationErrors();
                assertThat(validationErrors.get("shopId")).isEqualTo("ShopId can not be null");
            }

            @Test
            void postItem_whenShopHasEmptyItems_receiveMessageOfEmptyErrorForItems() {
                PluginItemDTO pItem = TestUtil.createValidPluginItem();
                pItem.setItems(List.of());
                ResponseEntity<ApiError> response = postPluginItem(pItem, ApiError.class);
                Map<String, String> validationErrors = Objects.requireNonNull(response.getBody()).getValidationErrors();
                assertThat(validationErrors.get("items")).isEqualTo("Items can not be empty");
            }

            @Test
            void postItem_whenPluginItemHasNullItemId_receiveBadRequest() {
                PluginItemDTO pItem = TestUtil.createValidPluginItem();
                ItemDTO itemDTO = TestUtil.createItemDTO();
                itemDTO.setItemId(null);
                pItem.setItems(List.of(itemDTO));
                ResponseEntity<Object> response = postPluginItem(pItem, Object.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            }

            @Test
            void postItem_whenPluginItemHasNullName_receiveBadRequest() {
                PluginItemDTO pItem = TestUtil.createValidPluginItem();
                ItemDTO itemDTO = TestUtil.createItemDTO();
                itemDTO.setName(null);
                pItem.setItems(List.of(itemDTO));
                ResponseEntity<Object> response = postPluginItem(pItem, Object.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            }

            @Test
            void postItem_whenPluginItemHasNullPic_receiveBadRequest() {
                PluginItemDTO pItem = TestUtil.createValidPluginItem();
                ItemDTO itemDTO = TestUtil.createItemDTO();
                itemDTO.setPic(null);
                pItem.setItems(List.of(itemDTO));
                ResponseEntity<Object> response = postPluginItem(pItem, Object.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            }

            @Test
            void postItem_whenPluginItemPicIsNotUrl_receiveBadRequest() {
                PluginItemDTO pItem = TestUtil.createValidPluginItem();
                ItemDTO itemDTO = TestUtil.createItemDTO();
                itemDTO.setPic("not-a-link");
                pItem.setItems(List.of(itemDTO));
                ResponseEntity<Object> response = postPluginItem(pItem, Object.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            }

            @Test
            void postItem_whenItemIdDuplicated_receiveBadRequest() {
                PluginItemDTO pItem = TestUtil.createValidPluginItem();
                postPluginItem(pItem, Object.class);
                ResponseEntity<Object> response = postPluginItem(pItem, Object.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            }

            @Test
            void postItem_whenItemIdDuplicated_receiveDuplicateItemId() {
                PluginItemDTO pItem = TestUtil.createValidPluginItem();
                postPluginItem(pItem, Object.class);
                ResponseEntity<ApiError> response = postPluginItem(pItem, ApiError.class);
                Map<String, String> validationErrors = Objects.requireNonNull(response.getBody()).getValidationErrors();
                assertThat(validationErrors.get("items[0].itemId")).isEqualTo("Item id is in database");
            }

        }

        public <T> ResponseEntity<T> postPluginItem(PluginItemDTO pItem, Class<T> responseType) {
            return testRestTemplate.postForEntity(API_1_0_PLUGIN_ITEMS, pItem, responseType);
        }

    }

}
