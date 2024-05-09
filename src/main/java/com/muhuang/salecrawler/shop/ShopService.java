package com.muhuang.salecrawler.shop;

import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ShopService {

    @Resource
    private ShopRepository shopRepository;

    public void save(Shop shop) {
        shopRepository.save(shop);
    }

    public Page<?> getShops() {
        PageRequest pageable = PageRequest.of(0, 10);
        return shopRepository.findAll(pageable);
    }

}
