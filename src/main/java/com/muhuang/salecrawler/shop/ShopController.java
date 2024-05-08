package com.muhuang.salecrawler.shop;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/1.0/shops")
public class ShopController {

    @Resource
    private ShopRepository shopRepository;

    @PostMapping
    public void saveShop(@RequestBody Shop shop) {
        shopRepository.save(shop);
    }

}
