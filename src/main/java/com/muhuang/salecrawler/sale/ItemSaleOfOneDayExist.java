package com.muhuang.salecrawler.sale;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ItemSaleOfOneDayExist extends RuntimeException {
}
