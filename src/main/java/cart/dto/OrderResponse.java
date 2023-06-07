package cart.dto;

import cart.domain.order.Order;
import cart.domain.order.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

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

    public static OrderResponse from(final Order order) {
        final List<OrderItem> orderItems = order.getOrderItems();
        final List<OrderedProduct> orderedProducts = orderItems.stream()
                .map(OrderedProduct::from)
                .collect(Collectors.toUnmodifiableList());
        return new OrderResponse(order.getId(), orderedProducts, PaymentDto.from(order));
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
