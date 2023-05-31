package cart.dto;

import java.util.List;

public class OrderRequest {

    private List<OrderItemRequest> orderItems;

    private Long spendPoint;

    private OrderRequest() {
    }

    public OrderRequest(List<OrderItemRequest> orderItems, Long spendPoint) {
        this.orderItems = orderItems;
        this.spendPoint = spendPoint;
    }

    public List<OrderItemRequest> getOrderItems() {
        return orderItems;
    }

    public Long getSpendPoint() {
        return spendPoint;
    }
}
