package com.muhuang.salecrawler.item;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * 商品销量抓取状态转换器
 *
 * @author Frank An
 */
@Converter
public class ItemSalesScrapingStatusConvert implements AttributeConverter<ItemSalesScrapingStatus, String> {
    @Override
    public String convertToDatabaseColumn(ItemSalesScrapingStatus attribute) {
        return attribute.getValue();
    }

    @Override
    public ItemSalesScrapingStatus convertToEntityAttribute(String dbData) {
        return ItemSalesScrapingStatus.valueOf(dbData);
    }
}
