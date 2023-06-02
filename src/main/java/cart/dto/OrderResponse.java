package cart.dto;

import java.util.List;

public class OrderResponse {
    private Long orderId;
    private List<OrderedProduct> orderedProducts;
    private PaymentDto payment;

    public OrderResponse() {
    }

    public OrderResponse(final Long orderId, final List<OrderedProduct> orderedProducts, final PaymentDto payment) {
        this.orderId = orderId;
        this.orderedProducts = orderedProducts;
        this.payment = payment;
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderedProduct> getOrderedProducts() {
        return orderedProducts;
    }

    public PaymentDto getPayment() {
        return payment;
    }
}
