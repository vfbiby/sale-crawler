package com.muhuang.salecrawler.taobao;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

/**
 * taobao 客户端
 *
 * @author Frank An
 */
public interface TaobaoHttpClient {

    /**
     * 获取月度销量
     *
     * @param goodsId 商品id
     * @param token   token
     * @return 月度销量
     */
    @GetExchange("/tb/ll/item/month/sale")
    TaobaoSaleMonthlyResult getMonthlySaleNum(@RequestParam("goodId") String goodsId,
                                              @RequestParam String token);


}
