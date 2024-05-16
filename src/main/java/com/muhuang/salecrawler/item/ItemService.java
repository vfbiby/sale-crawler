package com.muhuang.salecrawler.item;

import com.muhuang.salecrawler.shop.Shop;
import com.muhuang.salecrawler.shop.ShopRepository;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    @Resource
    private ItemRepository itemRepository;

    @Resource
    private ShopRepository shopRepository;

    public void save(Item item) {
        Shop inDB = shopRepository.findByOutShopId(item.getShop().getOutShopId());
        item.setShop(inDB);
        setPublishedAt(item);
        itemRepository.save(item);
    }

    private static void setPublishedAt(Item item) {
        item.setPublishedAt(new Date());
    }

    public void saveAll(List<Item> collect) {
        collect.stream().peek(ItemService::setPublishedAt).collect(Collectors.toList());
        itemRepository.saveAll(collect);
    }

    public Page<Item> getUsers() {
        Pageable pageRequest = PageRequest.of(0, 5);
        return itemRepository.findAll(pageRequest);
    }
}
