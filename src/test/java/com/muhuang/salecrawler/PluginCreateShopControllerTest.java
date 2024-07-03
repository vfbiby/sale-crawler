package com.muhuang.salecrawler;

import com.muhuang.salecrawler.cate.Cate;
import com.muhuang.salecrawler.cate.CateRepository;
import com.muhuang.salecrawler.item.Item;
import com.muhuang.salecrawler.item.ItemRepository;
import com.muhuang.salecrawler.item.PluginItemDTO;
import com.muhuang.salecrawler.shop.Shop;
import com.muhuang.salecrawler.shop.ShopRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.muhuang.salecrawler.PluginCreateItemControllerTest.API_1_0_PLUGIN_ITEMS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PluginCreateShopControllerTest {

    @Resource
    private CateRepository cateRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Resource
    private ShopRepository shopRepository;

    @Resource
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
        cateRepository.deleteAll();
        shopRepository.deleteAll();
    }

    @Nested
    class HappyPath {

        @Test
        void postPluginItem_whenItemHasValidShop_receiveOK() {
            PluginItemDTO pItem = TestUtil.createValidPluginItem();
            ResponseEntity<Object> response = postPluginItem(pItem, Object.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @Test
        void postPluginItem_whenItemHasValidShop_shopSaveToDatabase() {
            PluginItemDTO pItem = TestUtil.createValidPluginItem();
            postPluginItem(pItem, Object.class);
            List<Shop> all = shopRepository.findAll();
            assertThat(all.size()).isEqualTo(1);
        }

        @Test
        void postPluginItem_whenItemHasValidShop_itemSaveToDatabaseWithShop() {
            PluginItemDTO pItem = TestUtil.createValidPluginItem();
            postPluginItem(pItem, Object.class);
            List<Item> item = itemRepository.findAll();
            assertThat(item.size()).isEqualTo(1);
        }

        @Test
        void postPluginItem_whenItemHasValidShop_cateSaveToDatabaseWithShop() {
            PluginItemDTO pItem = TestUtil.createValidPluginItem();
            postPluginItem(pItem, Object.class);
            Cate cate = cateRepository.findByOutCateId(1779767080);
            assertThat(cate).isNotNull();
        }

        @Test
        void postPluginItem_whenItemHasValidShop_parentCateSaveToDatabaseWithShop() {
            PluginItemDTO pItem = TestUtil.createValidPluginItem();
            postPluginItem(pItem, Object.class);
            Cate cate = cateRepository.findByOutCateId(1779767080);
            assertThat(cate.getParent().getOutCateId()).isEqualTo(1772652848);
        }

    }

    @Nested
    class SadPath {

    }

    public <T> ResponseEntity<T> postPluginItem(PluginItemDTO pItem, Class<T> responseType) {
        return testRestTemplate.postForEntity(API_1_0_PLUGIN_ITEMS, pItem, responseType);
    }

}
