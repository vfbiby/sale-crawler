package com.muhuang.salecrawler;

import com.muhuang.salecrawler.cate.Cate;
import com.muhuang.salecrawler.cate.CateRepository;
import com.muhuang.salecrawler.item.Item;
import com.muhuang.salecrawler.item.ItemRepository;
import com.muhuang.salecrawler.item.PluginItemDTO;
import com.muhuang.salecrawler.shop.ShopRepository;
import jakarta.annotation.Resource;
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
import static org.assertj.core.api.Assertions.catchIndexOutOfBoundsException;

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

    @BeforeEach
    public void cleanup() {
        itemRepository.deleteAll();
        cateRepository.deleteAll();
        shopRepository.deleteAll();
    }

    @Nested
    class HappyPath {

        @Test
        void postItem_whenPluginItemHasCateId_cateIdSaveToDatabase() {
            PluginItemDTO pItem = TestUtil.createValidPluginItem();
            postPluginItem(pItem, Object.class);
            Cate cate = cateRepository.findByOutCateId(1779767080);
            assertThat(cate).isNotNull();
        }

        @Test
        void postItem_whenPluginItemHasCateName_cateNameSaveToDatabase() {
            PluginItemDTO pItem = TestUtil.createValidPluginItem();
            postPluginItem(pItem, Object.class);
            Cate cate = cateRepository.findByOutCateId(1779767080);
            assertThat(cate.getCateName()).isEqualTo("06/25福利回馈");
        }


        @Test
        void postItem_whenPluginItemHasParentCateId_parentCateIdSaveToDatabase() {
            PluginItemDTO pItem = TestUtil.createValidPluginItem();
            postPluginItem(pItem, Object.class);
            Cate cate = cateRepository.findByOutCateId(1772652848);
            assertThat(cate).isNotNull();
        }

        @Test
        void postItem_whenPluginItemHasParentCateName_parentCateNameSaveToDatabase() {
            PluginItemDTO pItem = TestUtil.createValidPluginItem();
            postPluginItem(pItem, Object.class);
            Cate cate = cateRepository.findByOutCateId(1772652848);
            assertThat(cate.getCateName()).isEqualTo("6月新品");
        }

        @Test
        void postItem_whenPluginItemHasCateIdWithParentCateId_cateIdSaveToDatabaseWithParentCateId() {
            PluginItemDTO pItem = TestUtil.createValidPluginItem();
            postPluginItem(pItem, Object.class);
            Cate cate = cateRepository.findByOutCateId(1779767080);
            assertThat(cate.getParent().getOutCateId()).isEqualTo(1772652848);
        }

        @Test
        void postItem_whenPluginItemHasCateId_itemSaveToDatabaseWithCateId() {
            PluginItemDTO pItem = TestUtil.createValidPluginItem();
            postPluginItem(pItem, Object.class);
            List<Item> items = itemRepository.findAll();
            assertThat(items.size()).isEqualTo(1);
        }

        @Test
        void postItem_whenPluginItemCateIdHadParentCateId_parentWillNotBeUpdated() {
            Cate parentCate = Cate.builder().cateName("New Arrival").outCateId(22).build();
            cateRepository.save(parentCate);
            Cate cate = Cate.builder().cateName("连衣裙").outCateId(1779767080).build();
            cate.setParent(parentCate);
            cateRepository.save(cate);
            PluginItemDTO pItem = TestUtil.createValidPluginItem();
            postPluginItem(pItem, Object.class);
            Cate byOutCateId = cateRepository.findByOutCateId(1779767080);
            assertThat(byOutCateId.getParent().getOutCateId()).isEqualTo(22);
        }

        @Test
        void postItem_whenPluginItemCateExistAndHadNoParentCate_parentWillBeUpdated() {
            Cate cate = Cate.builder().cateName("连衣裙").outCateId(1779767080).build();
            cateRepository.save(cate);
            PluginItemDTO pItem = TestUtil.createValidPluginItem();
            postPluginItem(pItem, Object.class);
            Cate byOutCateId = cateRepository.findByOutCateId(1779767080);
            assertThat(byOutCateId.getParent().getOutCateId()).isEqualTo(1772652848);
        }

        @Test
        void postItem_whenPluginItemCateIdExistInDb_cateNotSaveToDatabase() {
            cateRepository.save(Cate.builder().outCateId(1779767080).cateName("NEW ARRIVAL").build());
            PluginItemDTO pItem = TestUtil.createValidPluginItem();
            pItem.setParentCatId(null);
            pItem.setParentCatName(null);
            postPluginItem(pItem, Object.class);
            List<Cate> cateList = cateRepository.findAll();
            assertThat(cateList.size()).isEqualTo(1);
        }

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
