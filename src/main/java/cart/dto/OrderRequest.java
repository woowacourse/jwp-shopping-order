package cart.dto;

import java.util.List;

public class OrderRequest {

    private Integer deliveryFee;
    private List<OrderItemRequest> orderItems;

    public OrderRequest(Integer deliveryFee, List<OrderItemRequest> orderItems) {
        this.deliveryFee = deliveryFee;
        this.orderItems = orderItems;
    }

    public Integer getDeliveryFee() {
        return deliveryFee;
    }

    public List<OrderItemRequest> getOrderItems() {
        return orderItems;
    }
}
