package com.muhuang.salecrawler.shop;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/1.0/shop")
public class ShopController {

    @PostMapping
    public void saveShop(){

    }

}
