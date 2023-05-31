package com.woowahan.techcourse.order.ui;

import com.woowahan.techcourse.common.ui.ErrorResponse;
import com.woowahan.techcourse.order.exception.OrderNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.woowahan.techcourse.order")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class OrderControllerAdvice {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleOrderNotFound(OrderNotFoundException e) {
        log.error("Order Not Found Exception", e);
        return ResponseEntity.notFound().build();
    }
}
