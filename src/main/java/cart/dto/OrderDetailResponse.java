package cart.dto;

import java.util.List;
import java.util.Objects;

public class OrderDetailResponse {
    private Long orderId;
    private List<OrderItemResponse> products;
    private Integer totalPrice;

    public OrderDetailResponse() {
    }

    public OrderDetailResponse(final Long orderId, final List<OrderItemResponse> products, final Integer totalPrice) {
        this.orderId = orderId;
        this.products = products;
        this.totalPrice = totalPrice;
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderItemResponse> getProducts() {
        return products;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderDetailResponse that = (OrderDetailResponse) o;
        return Objects.equals(orderId, that.orderId)
                && Objects.equals(products, that.products)
                && Objects.equals(totalPrice, that.totalPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, products, totalPrice);
    }

    @Override
    public String toString() {
        return "OrderDetailResponse{" +
                "orderId=" + orderId +
                ", products=" + products +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
