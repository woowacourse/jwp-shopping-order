package cart.domain.order;

import java.util.List;

public class OrderItems {

    private final List<OrderItem> items;

    public OrderItems(final List<OrderItem> items) {
        this.items = items;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public Long getTotalPrice() {
        return items.stream()
            .mapToLong(item -> (long) item.getProduct().getPrice() * item.getQuantity())
            .sum();
    }
}
