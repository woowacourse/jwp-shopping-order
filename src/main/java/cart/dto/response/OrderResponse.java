package cart.dto.response;

import java.util.List;

public class OrderResponse {

    private final long orderId;
    private final String orderDate;
    private final long totalProductsPrice;
    private final long shippingFee;
    private final List<OrderDetailsDto> orderDetails;

    public OrderResponse(final long orderId, final String orderDate, final long totalProductsPrice, final long shippingFee, final List<OrderDetailsDto> orderDetails) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.totalProductsPrice = totalProductsPrice;
        this.shippingFee = shippingFee;
        this.orderDetails = orderDetails;
    }

    public long getOrderId() {
        return orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public long getTotalProductsPrice() {
        return totalProductsPrice;
    }

    public List<OrderDetailsDto> getOrderDetails() {
        return orderDetails;
    }

    public long getShippingFee() {
        return shippingFee;
    }
}
