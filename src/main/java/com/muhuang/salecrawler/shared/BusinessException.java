package com.muhuang.salecrawler.shared;

import lombok.NoArgsConstructor;

/**
 * 业务异常
 *
 * @author Frank An
 */
@NoArgsConstructor
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
