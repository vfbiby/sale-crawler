package com.muhuang.salecrawler.shop;

import jakarta.annotation.Priority;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ShopService {

    @Resource
    private ShopRepository shopRepository;

    public Shop save(Shop shop) {
        return shopRepository.save(shop);
    }

}
