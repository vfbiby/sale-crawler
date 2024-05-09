package com.muhuang.salecrawler.item;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ItemService {

    @Resource
    private ItemRepository itemRepository;

    public void save(Item item) {
        item.setPublishedAt(new Date());
        itemRepository.save(item);
    }
}
