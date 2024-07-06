package com.muhuang.salecrawler.item.service;

import com.muhuang.salecrawler.item.ItemNotFoundException;
import com.muhuang.salecrawler.item.entity.Item;
import com.muhuang.salecrawler.item.entity.ItemSalesScrapingSchedule;
import com.muhuang.salecrawler.item.repository.ItemSalesScrapingScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 商品销量抓取计划 Service
 *
 * @author Frank An
 */
@Service
@RequiredArgsConstructor
public class ItemSalesScrapingScheduleService {

    private final ItemSalesScrapingScheduleRepository itemSalesScrapingScheduleRepository;
    private final ItemService itemService;

    /**
     * 创建一个商品销量抓取计划
     *
     * @param outItemId 商品id
     */
    public void create(String outItemId) {
        Item item = itemService.getByOutItemId(outItemId)
                .orElseThrow(ItemNotFoundException::new);
        itemSalesScrapingScheduleRepository.save(ItemSalesScrapingSchedule.builder()
                .item(item)
                .createdAt(LocalDateTime.now())
                .build());
    }
}
