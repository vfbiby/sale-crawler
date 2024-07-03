package com.muhuang.salecrawler;

import com.muhuang.salecrawler.cate.Cate;
import com.muhuang.salecrawler.cate.CateRepository;
import com.muhuang.salecrawler.item.Item;
import com.muhuang.salecrawler.item.ItemRepository;
import com.muhuang.salecrawler.item.PluginItemDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.muhuang.salecrawler.PluginCreateItemControllerTest.API_1_0_PLUGIN_ITEMS;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PluginCreateCateControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Resource
    private CateRepository cateRepository;

    @Resource
    private ItemRepository itemRepository;

    @Resource
    private ShopRepository shopRepository;

    @Resource
    private ShopService shopService;

    @AfterEach
    public void cleanup() {
        itemRepository.deleteAll();
        shopRepository.deleteAll();
        cateRepository.deleteAll();
    }

    @BeforeEach
    public void setup() {
        insertValidShop();
    }

    @Nested
    class HappyPath {

        @Test
        void postItem_whenPluginItemHasCateId_cateIdSaveToDatabase() {
            PluginItemDTO pItem = TestUtil.createValidPluginItem();
            postPluginItem(pItem, Object.class);
            Cate cate = cateRepository.findAll().get(1);
            assertThat(cate.getOutCateId()).isEqualTo(1779767080);
        }

        @Test
        void postItem_whenPluginItemHasCateName_cateNameSaveToDatabase() {
            PluginItemDTO pItem = TestUtil.createValidPluginItem();
            postPluginItem(pItem, Object.class);
            Cate cate = cateRepository.findAll().get(1);
            assertThat(cate.getCateName()).isEqualTo("06/25福利回馈");
        }


        @Test
        void postItem_whenPluginItemHasParentCateId_parentCateIdSaveToDatabase() {
            PluginItemDTO pItem = TestUtil.createValidPluginItem();
            postPluginItem(pItem, Object.class);
            Cate cate = cateRepository.findAll().get(0);
            assertThat(cate.getOutCateId()).isEqualTo(1772652848);
        }

        @Test
        void postItem_whenPluginItemHasParentCateName_parentCateNameSaveToDatabase() {
            PluginItemDTO pItem = TestUtil.createValidPluginItem();
            postPluginItem(pItem, Object.class);
            Cate cate = cateRepository.findAll().get(0);
            assertThat(cate.getCateName()).isEqualTo("6月新品");
        }

        @Test
        void postItem_whenPluginItemHasCateIdWithParentCateId_cateIdSaveToDatabaseWithParentCateId() {
            PluginItemDTO pItem = TestUtil.createValidPluginItem();
            postPluginItem(pItem, Object.class);
            Cate cate = cateRepository.findAll().get(1);
            assertThat(cate.getParent().getOutCateId()).isEqualTo(1772652848);
        }

        @Test
        void postItem_whenPluginItemHasCateId_itemSaveToDatabaseWithCateId() {
            PluginItemDTO pItem = TestUtil.createValidPluginItem();
            postPluginItem(pItem, Object.class);
            Item item = itemRepository.findAll().get(0);
            assertThat(item.getCate().getOutCateId()).isEqualTo(1779767080);
        }

        @Test
        void postItem_whenPluginItemCateIdExistInDb_cateNotSaveToDatabase() {
            PluginItemDTO pItem = TestUtil.createValidPluginItem();
            pItem.setParentCatId(null);
            pItem.setParentCatName(null);
            postPluginItem(pItem, Object.class);
            List<Cate> cateList = cateRepository.findAll();
            System.out.println(cateList);
            assertThat(cateList.size()).isEqualTo(1);
        }

    }

    void insertValidShop() {
        PluginItemDTO pluginItemDTO = TestUtil.createValidPluginItemShop();
        Shop shop = Shop.builder().outShopId(pluginItemDTO.getShopId())
                .shopName(pluginItemDTO.getShopName())
                .shopUrl(pluginItemDTO.getShopUrl()).build();
        shopService.save(shop);
    }

    @Nested
    class SadPath {

        @Test
        void postItem_whenPluginItemParentCateIdIsNull_parentCateNotSaveToDatabase() {
            PluginItemDTO pItem = TestUtil.createValidPluginItem();
            pItem.setParentCatId(null);
            postPluginItem(pItem, Object.class);
            List<Cate> cateList = cateRepository.findAll();
            assertThat(cateList.size()).isEqualTo(1);
        }

        @Test
        void postItem_whenPluginItemCateIdIsNull_CateNotSaveToDatabase() {
            cateRepository.deleteAll();
            PluginItemDTO pItem = TestUtil.createValidPluginItem();
            pItem.setCatId(null);
            postPluginItem(pItem, Object.class);
            List<Cate> cateList = cateRepository.findAll();
            assertThat(cateList.size()).isEqualTo(0);
        }

    }

    public <T> ResponseEntity<T> postPluginItem(PluginItemDTO pItem, Class<T> responseType) {
        return testRestTemplate.postForEntity(API_1_0_PLUGIN_ITEMS, pItem, responseType);
    }

}
