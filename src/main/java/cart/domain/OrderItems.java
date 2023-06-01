package cart.domain;

import java.util.List;

public class OrderItems {

    private final List<OrderItem> orderItems;

    public OrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
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
}
