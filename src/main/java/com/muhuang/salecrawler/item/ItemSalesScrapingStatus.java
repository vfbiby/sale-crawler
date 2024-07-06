package com.muhuang.salecrawler.item;

import lombok.Getter;

/**
 * 商品销量抓取状态
 *
 * @author Frank An
 */
@Getter
public enum ItemSalesScrapingStatus {
    CREATED("created"),
    SUCCESS("success"),
    FAILED("failed"),
    RUNNING("running");

    private final String value;

    ItemSalesScrapingStatus(String value) {
        this.value = value;
    }
}
