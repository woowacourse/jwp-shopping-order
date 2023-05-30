package cart.dto.response;

import java.util.List;

public class OrderResponse {

    private final long orderId;
    private final String orderDate;
    private final long totalPrice;
    private final List<OrderDetailsDto> orderDetails;

    public OrderResponse(final long orderId, final String orderDate, final long totalPrice, final List<OrderDetailsDto> orderDetails) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.orderDetails = orderDetails;
    }

    public long getOrderId() {
        return orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public List<OrderDetailsDto> getOrderDetails() {
        return orderDetails;
    }
}
