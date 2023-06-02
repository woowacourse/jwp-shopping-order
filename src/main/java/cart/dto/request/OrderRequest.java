package cart.dto.request;

import java.util.List;

public class OrderRequest {

    private Integer deliveryFee;
    private List<OrderItemRequest> orderItemRequests;

    public OrderRequest() {
    }

    public OrderRequest(Integer deliveryFee, List<OrderItemRequest> orderItemRequests) {
        this.deliveryFee = deliveryFee;
        this.orderItemRequests = orderItemRequests;
    }

    public List<OrderItemRequest> getOrderItemRequests() {
        return orderItemRequests;
    }

    public Integer getDeliveryFee() {
        return deliveryFee;
    }
}
