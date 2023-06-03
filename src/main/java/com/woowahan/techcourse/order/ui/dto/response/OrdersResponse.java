package com.woowahan.techcourse.order.ui.dto.response;

import com.woowahan.techcourse.order.domain.OrderResult;
import java.util.List;
import java.util.stream.Collectors;

public class OrdersResponse {

    private List<OrderResponse> orders;

    private OrdersResponse() {
    }

    public OrdersResponse(List<OrderResponse> orders) {
        this.orders = orders;
    }

    public static OrdersResponse from(List<OrderResult> orderResults) {
        return new OrdersResponse(orderResults.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList()));
    }

    public List<OrderResponse> getOrders() {
        return orders;
    }
}
