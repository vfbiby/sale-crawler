package com.muhuang.salecrawler.item;

import com.muhuang.salecrawler.shared.GenericResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/1.0/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    GenericResponse createItem(@RequestBody Item item) {
        itemService.save(item);
        return new GenericResponse("Item saved");
    }

}
