package com.woowahan.techcourse.cart.ui;

import com.woowahan.techcourse.cart.exception.AuthenticationException;
import com.woowahan.techcourse.cart.exception.IllegalMemberException;
import com.woowahan.techcourse.common.ui.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "com.woowahan.techcourse.cart")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ControllerExceptionHandler {

    Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handlerAuthenticationException(AuthenticationException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("로그인이 필요합니다."));
    }

    @ExceptionHandler(IllegalMemberException.class)
    public ResponseEntity<ErrorResponse> handleException(IllegalMemberException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("권한이 없는 사용자가 접근했습니다"));
    }
}
