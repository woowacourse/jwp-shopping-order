package cart.domain;

import java.util.List;
import java.util.Objects;

public class OrderItems {

    private final List<OrderItem> orderItems;

    private OrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public static OrderItems from(List<OrderItem> orderItems) {
        return new OrderItems(orderItems);
    }

    public Long calculateOrderPrice() {
        return orderItems.stream()
                .map(OrderItem::getProduct)
                .mapToLong(Product::getPrice)
                .sum();
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItems that = (OrderItems) o;
        return Objects.equals(orderItems, that.orderItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderItems);
    }
}
