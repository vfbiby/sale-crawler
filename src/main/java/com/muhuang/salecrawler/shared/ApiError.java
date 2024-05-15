package com.muhuang.salecrawler.shared;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    private final long timestamp = new Date().getTime();

    private int status;

    private String message;

    private String url;

    private Map<String, String> validationErrors;

}