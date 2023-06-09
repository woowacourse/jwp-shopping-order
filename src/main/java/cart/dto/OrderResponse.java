package cart.dto;

import cart.domain.Order;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private final Long id;
    private final LocalDateTime orderTime;
    private final List<OrderItemResponse> productList;

    public OrderResponse(Long id, LocalDateTime orderTime, List<OrderItemResponse> productList) {
        this.id = id;
        this.orderTime = orderTime;
        this.productList = productList;
    }

    public static OrderResponse from(Order order) {
        List<OrderItemResponse> productList = order.getOrderItems().stream()
                .map(OrderItemResponse::from)
                .collect(Collectors.toList());
        return new OrderResponse(order.getId(), order.getOrderTime(), productList);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public List<OrderItemResponse> getProductList() {
        return productList;
    }
}
