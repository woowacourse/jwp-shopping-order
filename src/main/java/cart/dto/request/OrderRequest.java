package cart.dto.request;

import java.util.List;

public class OrderRequest {

    private Integer deliveryFee;
    private List<OrderItemRequest> orderItems;

    public OrderRequest() {
    }

    public OrderRequest(Integer deliveryFee, List<OrderItemRequest> orderItems) {
        this.deliveryFee = deliveryFee;
        this.orderItems = orderItems;
    }

    public List<OrderItemRequest> getOrderItems() {
        return orderItems;
    }

    public Integer getDeliveryFee() {
        return deliveryFee;
    }
}
