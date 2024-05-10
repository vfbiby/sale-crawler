package com.muhuang.salecrawler.item;

import com.muhuang.salecrawler.shared.GenericResponse;
import com.muhuang.salecrawler.shop.Shop;
import com.muhuang.salecrawler.shop.ShopRepository;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/1.0/items")
public class ItemController {

    @Resource
    private ItemService itemService;

    @Resource
    private final ShopRepository shopRepository;

    public ItemController(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @PostMapping
    GenericResponse createItem(@Valid @RequestBody Item item) {
        try {
            Shop inDB = shopRepository.findByOutShopId(item.getShop().getOutShopId());
            item.setShop(inDB);
        } catch (Exception ignored) {

        }
        itemService.save(item);
        return new GenericResponse("Item saved");
    }

}
