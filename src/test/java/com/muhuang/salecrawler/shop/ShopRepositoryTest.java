package com.muhuang.salecrawler.shop;

import com.muhuang.salecrawler.shop.Shop;
import com.muhuang.salecrawler.shop.ShopRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ShopRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Resource
    private ShopRepository shopRepository;

    @Test
    void findByOutShopId_whenShopExists_returnShop(){
        Shop shop = Shop.builder().outShopId("111111111").shopName("james shop").shopUrl("https://sky.taobao.com").build();
        testEntityManager.persist(shop);
        Shop inDB = shopRepository.findByOutShopId(shop.getOutShopId());
        assertThat(inDB).isNotNull();
    }

    @Test
    void findByOutShopId_whenShopDoesNotExists_returnNull(){
        Shop inDB = shopRepository.findByOutShopId("not-exists-shop-id");
        assertThat(inDB).isNull();
    }

}
