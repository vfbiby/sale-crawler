package com.muhuang.salecrawler.shop;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ShopIsNotExistInDbException extends RuntimeException {
    private String message;

    public ShopIsNotExistInDbException(String message) {
        this.message = message;
    }
}
