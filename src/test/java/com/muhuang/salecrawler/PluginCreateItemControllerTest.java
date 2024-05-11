package com.muhuang.salecrawler;

import com.muhuang.salecrawler.item.Item;
import com.muhuang.salecrawler.item.ItemRepository;
import com.muhuang.salecrawler.item.PluginItemDTO;
import com.muhuang.salecrawler.shared.ApiError;
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

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PluginCreateItemControllerTest {

    public static final String API_1_0_PLUGIN_ITEMS = "/api/1.0/plugin-items";
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Resource
    private ItemRepository itemRepository;

    @Nested
    class Create {

        @BeforeEach
        public void cleanup() {
            itemRepository.deleteAll();
        }

        @Nested
        class HappyPath {

            @Test
            void postItem_whenShopAndItemIsValid_receiveOK() {
                PluginItemDTO pItem = createValidPluginItem();
                ResponseEntity<Object> response = postPluginItem(pItem, Object.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            }

            @Test
            void postItem_whenShopAndItemIsValid_itemSaveToDatabase() {
                PluginItemDTO pItem = createValidPluginItem();
                postPluginItem(pItem, Object.class);
                assertThat(itemRepository.count()).isEqualTo(1);
            }

            @Test
            void postItem_whenShopAndItemIsValid_receiveSuccessMessage() {
                PluginItemDTO pItem = createValidPluginItem();
                ResponseEntity<Object> response = postPluginItem(pItem, Object.class);
                assertThat(response.getBody()).isNotNull();
            }

        }

        @Nested
        class SadPath {

            @Test
            void postItem_whenShopHasNullShopId_receiveBadRequest() {
                PluginItemDTO pItem = createValidPluginItem();
                pItem.setShopId(null);
                ResponseEntity<Object> response = postPluginItem(pItem, Object.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            }

            @Test
            void postItem_whenShopIsValidButShopIsNotInDB_receiveBadRequest() {
                PluginItemDTO pItem = createValidPluginItem();
                pItem.setShopId("not-in-db-shop");
                ResponseEntity<Object> response = postPluginItem(pItem, Object.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            }

            @Test
            void postItem_whenShopIsInvalid_receiveApiError() {
                PluginItemDTO pItem = new PluginItemDTO();
                ResponseEntity<ApiError> response = postPluginItem(pItem, ApiError.class);
                assertThat(response.getBody().getUrl()).isEqualTo(API_1_0_PLUGIN_ITEMS);
            }

            @Test
            void postItem_whenShopIsInvalid_receiveApiErrorWithValidationErrors() {
                PluginItemDTO pItem = new PluginItemDTO();
                ResponseEntity<ApiError> response = postPluginItem(pItem, ApiError.class);
                assertThat(response.getBody().getValidationErrors().size()).isEqualTo(1);
            }

        }

        private <T> ResponseEntity<T> postPluginItem(PluginItemDTO pItem, Class<T> responseType) {
            return testRestTemplate.postForEntity(API_1_0_PLUGIN_ITEMS, pItem, responseType);
        }

        private static PluginItemDTO createValidPluginItem() {
            PluginItemDTO pItem = PluginItemDTO.builder().shopId("3423343434")
                    .shopName("TT坏坏")
                    .shopUrl("https://shop105703949.taobao.com").build();
            Item item = Item.builder().itemId("779612411768")
                    .name("TT坏坏针织无袖长裙搭配吊带裙两件套女休闲度假风宽松设计感套装").build();
            ArrayList<Item> items = new ArrayList<>();
            items.add(item);
            pItem.setItem(items);
            return pItem;
        }

    }

}
