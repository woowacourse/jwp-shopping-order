package cart.dto.request;

import java.beans.ConstructorProperties;
import java.util.List;

public class OrderRequest {

    private final List<OrderItemRequest> orderItems;


    @ConstructorProperties(value = {"orderItems"})
    public OrderRequest(final List<OrderItemRequest> orderItems) {
        this.orderItems = orderItems;
    }

    public List<OrderItemRequest> getOrderItems() {
        return orderItems;
    }
}
