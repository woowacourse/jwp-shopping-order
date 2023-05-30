package com.woowahan.techcourse.common.ui;

public abstract class ApiException extends RuntimeException {

    protected ApiException(String message) {
        super(message);
    }

    protected ApiException() {
    }
}
