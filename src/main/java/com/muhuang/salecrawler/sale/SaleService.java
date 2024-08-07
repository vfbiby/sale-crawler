package com.muhuang.salecrawler.sale;

import com.muhuang.salecrawler.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Frank An
 */
@Service
@RequiredArgsConstructor
public class SaleService {
    private SaleRepository saleRepository;

    /**
     * 保存月度销量
     *
     * @param monthlySaleNum 月度销量
     * @param item           商品
     */
    public void save(int monthlySaleNum, Item item) {
        Sale sale = Sale.builder()
                .item(item)
                .saleDate(new Date())
                .sellCount(monthlySaleNum)
                .build();
        saleRepository.save(sale);
    }
}
