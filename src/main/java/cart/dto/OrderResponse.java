package cart.dto;

import cart.domain.Order;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderResponse {
    private Long id;
    private List<OrderItemResponse> products;

    public OrderResponse() {
    }

    public OrderResponse(final Long id, final List<OrderItemResponse> products) {
        this.id = id;
        this.products = products;
    }

    public static OrderResponse from(final Order order) {
        List<OrderItemResponse> orderItemResponses = order.getOrderItems()
                .stream()
                .map(OrderItemResponse::from)
                .collect(Collectors.toList());
        return new OrderResponse(
                order.getId(),
                orderItemResponses
        );
    }

    public Long getId() {
        return id;
    }

    public List<OrderItemResponse> getProducts() {
        return products;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderResponse that = (OrderResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(products, that.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, products);
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "orderId=" + id +
                ", products=" + products +
                '}';
    }
}
