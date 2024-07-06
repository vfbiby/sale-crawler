package com.muhuang.salecrawler.item.controller;

import com.muhuang.salecrawler.item.entity.Item;
import com.muhuang.salecrawler.item.service.ItemService;
import com.muhuang.salecrawler.shared.GenericResponse;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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

    @GetMapping
    Page<?> getItems(@RequestParam Optional<String> sortBy, @RequestParam Optional<Sort.Direction> direction) {
        return itemService.getUsers(
                direction.orElse(Sort.Direction.DESC),
                sortBy.orElse("id"));
    }

}
