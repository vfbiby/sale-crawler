package com.muhuang.salecrawler.sale;

import com.muhuang.salecrawler.item.entity.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Frank An
 */
@Service
@RequiredArgsConstructor
public class SaleService {
    private final SaleRepository saleRepository;

    /**
     * 保存月度销量
     *
     * @param monthlySaleNum 月度销量
     * @param item           商品
     */
    public void save(int monthlySaleNum, Item item) {
        Sale sale = Sale.builder()
                .item(item)
                .saleDate(LocalDateTime.now())
                .number(monthlySaleNum)
                .build();
        saleRepository.save(sale);
    }
}
