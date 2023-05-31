package cart.dto.request;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;
import java.util.List;

public class OrderRequest {

    private final List<OrderItemRequest> orderItems;
    private final LocalDateTime orderTime;


    @ConstructorProperties(value = {"orderItems", "orderTime"})
    public OrderRequest(final List<OrderItemRequest> orderItems, final LocalDateTime orderTime) {
        this.orderItems = orderItems;
        this.orderTime = orderTime;
    }


    public List<OrderItemRequest> getOrderItems() {
        return orderItems;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }
}
