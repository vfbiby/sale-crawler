package com.muhuang.salecrawler.shop;

import com.muhuang.salecrawler.shared.GenericResponse;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/1.0/shops")
public class ShopController {

    @Resource
    private ShopService shopService;

    @PostMapping
    public GenericResponse createShop(@Valid @RequestBody Shop shop) {
        shopService.save(shop);
        return new GenericResponse("user saved!");
    }

}
