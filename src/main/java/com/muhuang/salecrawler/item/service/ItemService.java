package com.muhuang.salecrawler.item.service;

import com.muhuang.salecrawler.item.entity.Item;
import com.muhuang.salecrawler.item.repository.ItemRepository;
import com.muhuang.salecrawler.shop.Shop;
import com.muhuang.salecrawler.shop.ShopRepository;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
        if (item.getPublishedAt() == null)
            item.setPublishedAt(LocalDate.now());
    }

    public void saveAll(List<Item> collect) {
        collect.stream().peek(ItemService::setPublishedAt).collect(Collectors.toList());
        itemRepository.saveAll(collect);
    }

    public Page<Item> getUsers(Sort.Direction direction, String sortBy) {
        PageRequest pageRequest = PageRequest.of(0, 5, direction, sortBy);
        return itemRepository.findAll(pageRequest);
    }

    /**
     * 根据 outItemId 查询商品
     * @param outItemId 商品 id
     * @return 商品
     */
    public Optional<Item> getByOutItemId(String outItemId) {
        return itemRepository.findByOutItemId(outItemId);
    }
}