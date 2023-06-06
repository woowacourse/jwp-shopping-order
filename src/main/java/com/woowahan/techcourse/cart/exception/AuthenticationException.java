package com.woowahan.techcourse.cart.exception;

import com.woowahan.techcourse.common.ui.ApiException;

public class AuthenticationException extends ApiException {

    public AuthenticationException() {
        super("인증되지 않은 사용자입니다.");
    }
}
