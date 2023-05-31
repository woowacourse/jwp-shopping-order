package cart.dto;

import java.util.List;

public class OrdersResponse {

    private List<OrderDetailResponse> orderDetails;

    public OrdersResponse(List<OrderDetailResponse> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public List<OrderDetailResponse> getOrderDetails() {
        return orderDetails;
    }
}
