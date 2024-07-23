package com.muhuang.salecrawler.schedule;

public class ItemCrawlFailedException extends RuntimeException{
    public ItemCrawlFailedException(String message) {
        super(message);
    }
}
