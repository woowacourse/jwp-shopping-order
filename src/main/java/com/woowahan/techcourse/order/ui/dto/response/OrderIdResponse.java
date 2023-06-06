package com.woowahan.techcourse.order.ui.dto.response;

public class OrderIdResponse {

    private final long orderId;

    public OrderIdResponse(long orderId) {
        this.orderId = orderId;
    }

    public long getOrderId() {
        return orderId;
    }
}
