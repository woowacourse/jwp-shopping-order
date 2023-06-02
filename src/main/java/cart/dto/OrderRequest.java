package cart.dto;

import java.util.List;

public class OrderRequest {
    List<OrderItem> orderItems;
    PaymentDto payment;

    public OrderRequest() {
    }

    public OrderRequest(final List<OrderItem> orderItems, final PaymentDto payment) {
        this.orderItems = orderItems;
        this.payment = payment;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public PaymentDto getPayment() {
        return payment;
    }
}
