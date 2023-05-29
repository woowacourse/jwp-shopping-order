package cart.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderItems {

    private final List<OrderItem> orderItems;

    public OrderItems(final List<OrderItem> orderItems) {
        this.orderItems = new ArrayList<>(orderItems);
    }

    public static OrderItems from(final List<CartItem> cartItems) {
        return new OrderItems(cartItems.stream()
                .map(OrderItem::from)
                .collect(Collectors.toList()));
    }

    public long sumPrice() {
        return orderItems.stream()
                .mapToLong(OrderItem::getPrice)
                .sum();
    }

    public int size() {
        return orderItems.size();
    }

    public List<OrderItem> getOrderItems() {
        return List.copyOf(orderItems);
    }
}
