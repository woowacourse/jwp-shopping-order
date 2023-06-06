package com.woowahan.techcourse.order.ui.dto.response;

import com.woowahan.techcourse.order.domain.Order;
import java.util.List;
import java.util.stream.Collectors;

public class OrdersResponse {

    private List<OrderResponse> orders;

    private OrdersResponse() {
    }

    public OrdersResponse(List<OrderResponse> orders) {
        this.orders = orders;
    }

    public static OrdersResponse from(List<Order> orderResults) {
        return new OrdersResponse(orderResults.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList()));
    }

    public List<OrderResponse> getOrders() {
        return orders;
    }
}
