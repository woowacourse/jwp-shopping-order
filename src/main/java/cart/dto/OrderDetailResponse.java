package cart.dto;

import cart.domain.Order;

import java.util.Objects;

public class OrderDetailResponse {

    private final OrderResponse order;
    private final int totalPrice;

    public OrderDetailResponse(OrderResponse order, int totalPrice) {
        this.order = order;
        this.totalPrice = totalPrice;
    }

    public static OrderDetailResponse of(Order order) {
        return new OrderDetailResponse(OrderResponse.of(order), order.getFinalPrice());
    }

    public OrderResponse getOrder() {
        return order;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderDetailResponse that = (OrderDetailResponse) o;
        return totalPrice == that.totalPrice && Objects.equals(order, that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, totalPrice);
    }

    @Override
    public String toString() {
        return "OrderDetailResponse{" +
                "order=" + order +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
