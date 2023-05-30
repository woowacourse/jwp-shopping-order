package cart.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private final Long id;
    private final LocalDateTime orderTime;
    private final List<OrderItemResponse> productList;

    public OrderResponse(Long id, LocalDateTime orderTime, List<OrderItemResponse> productList) {
        this.id = id;
        this.orderTime = orderTime;
        this.productList = productList;
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
