package cart.dto;

import java.util.List;
import java.util.Objects;

public class AllOrderResponse {
    private List<OrderResponse> orders;

    public AllOrderResponse() {
    }

    public AllOrderResponse(final List<OrderResponse> orders) {
        this.orders = orders;
    }

    public List<OrderResponse> getOrders() {
        return orders;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final AllOrderResponse that = (AllOrderResponse) o;
        return Objects.equals(orders, that.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orders);
    }

    @Override
    public String toString() {
        return "AllOrderResponse{" +
                "orders=" + orders +
                '}';
    }
}
