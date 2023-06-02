package cart.dto.response;

import java.util.List;

public class OrderInfo {
    private long orderId;
    private List<OrderProductInfo> orderItems;

    public OrderInfo() {
    }

    public OrderInfo(final long orderId, final List<OrderProductInfo> orderItems) {
        this.orderId = orderId;
        this.orderItems = orderItems;
    }

    public long getOrderId() {
        return orderId;
    }

    public List<OrderProductInfo> getOrderItems() {
        return orderItems;
    }
}
