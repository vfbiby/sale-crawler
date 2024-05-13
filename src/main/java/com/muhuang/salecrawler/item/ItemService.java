package com.muhuang.salecrawler.item;

import com.muhuang.salecrawler.shop.Shop;
import com.muhuang.salecrawler.shop.ShopRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ItemService {

    @Resource
    private ItemRepository itemRepository;

    @Resource
    private ShopRepository shopRepository;

    public void save(Item item) {
        Shop inDB = shopRepository.findByOutShopId(item.getShop().getOutShopId());
        item.setShop(inDB);
        item.setPublishedAt(new Date());
        itemRepository.save(item);
    }

}
