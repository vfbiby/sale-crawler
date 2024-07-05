package com.muhuang.salecrawler.taobao;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Frank An
 */
public record TaobaoSaleMonthlyResult(
        int code,
        String msg,
        @JsonProperty("biz30day") int saleMonthlyNum,
        String flag) {
}
