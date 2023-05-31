package cart.dto;

import java.util.List;
import java.util.Objects;

public class OrderResponse {
    private Long orderId;
    private List<OrderItemResponse> products;

    public OrderResponse() {
    }

    public OrderResponse(final Long orderId, final List<OrderItemResponse> products) {
        this.orderId = orderId;
        this.products = products;
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderItemResponse> getProducts() {
        return products;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderResponse that = (OrderResponse) o;
        return Objects.equals(orderId, that.orderId) && Objects.equals(products, that.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, products);
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "orderId=" + orderId +
                ", products=" + products +
                '}';
    }
}
