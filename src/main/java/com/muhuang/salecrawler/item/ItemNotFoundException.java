package com.muhuang.salecrawler.item;

import com.muhuang.salecrawler.shared.BusinessException;

/**
 * @author Frank An
 */
public class ItemNotFoundException extends BusinessException {
    public ItemNotFoundException() {
        super("商品不存在");
    }
}
