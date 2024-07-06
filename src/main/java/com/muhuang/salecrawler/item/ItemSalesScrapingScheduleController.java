package com.muhuang.salecrawler.item;

import com.muhuang.salecrawler.shared.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 商品销量抓取 Controller
 *
 * @author Frank An
 */
@RestController
@RequestMapping("/api/1.0/items")
@RequiredArgsConstructor
public class ItemSalesScrapingScheduleController {
    private final ItemSalesScrapingScheduleRepository itemSalesScrapingScheduleRepository;
    private final ItemService itemService;

    /**
     * 创建一个商品销量抓取计划
     *
     * @param outItemId 商品id
     */
    @PostMapping("/{itemId}/schedule")
    public GenericResponse createItemSalesSchedule(@PathVariable(value = "itemId") String outItemId) {
        Item item = itemService.getByOutItemId(outItemId)
                .orElseThrow(ItemNotFoundException::new);
        itemSalesScrapingScheduleRepository.save(ItemSalesScrapingSchedule.builder()
                .item(item)
                .createdAt(LocalDateTime.now())
                .build());
        return new GenericResponse("商品销量抓取计划创建成功");
    }
}
