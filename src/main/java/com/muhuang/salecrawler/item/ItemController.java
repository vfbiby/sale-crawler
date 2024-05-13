package com.muhuang.salecrawler.item;

import com.muhuang.salecrawler.shared.GenericResponse;
import com.muhuang.salecrawler.shop.Shop;
import com.muhuang.salecrawler.shop.ShopIsNotExistInDbException;
import com.muhuang.salecrawler.shop.ShopRepository;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/1.0/items")
public class ItemController {

    @Resource
    private ItemService itemService;

    @PostMapping
    GenericResponse createItem(@Valid @RequestBody Item item) {
        itemService.save(item);
        return new GenericResponse("Item saved");
    }

}
