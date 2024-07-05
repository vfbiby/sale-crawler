package com.muhuang.salecrawler.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 收藏商品
 * @author Frank An
 */
@RestController
@RequestMapping("/api/1.0/items")
@RequiredArgsConstructor
public class FavoriteItemController {


    private final ItemService itemService;


    /**
     * 收藏
     *
     * @param itemId 商品id
     */
    @PostMapping("/{itemId}/favorite")
    void favoriteItem(@PathVariable Long itemId) {
        itemService.favorite(itemId);
    }

}
