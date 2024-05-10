package com.muhuang.salecrawler.shop;

import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ShopService {

    @Resource
    private ShopRepository shopRepository;

    public Shop save(Shop shop) {
        return shopRepository.save(shop);
    }

    public Page<Shop> getShops() {
        PageRequest pageable = PageRequest.of(0, 10);
        return shopRepository.findAll(pageable);
    }

}
