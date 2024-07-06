package com.muhuang.salecrawler.shared;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Frank An
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 处理业务异常
     *
     * @param be      业务异常
     * @param request 请求
     * @return 响应 {@link ApiError}
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBusinessException(BusinessException be, HttpServletRequest request) {
        log.error(be.getMessage(), be);
        return new ApiError(HttpStatus.BAD_REQUEST.value(), be.getMessage(), request.getServletPath());
    }
}
