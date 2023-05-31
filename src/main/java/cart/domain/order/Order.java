package cart.domain.order;

import cart.domain.product.Price;
import java.util.List;

public class Order {

    private final Long id;
    private final List<OrderItem> orderItems;

    public Order(final List<OrderItem> orderItemList) {
        this(null, orderItemList);
    }

    public Order(final Long id, final List<OrderItem> orderItemList) {
        this.id = id;
        this.orderItems = orderItemList;
    }

    public Price getTotalPrice() {
        return orderItems.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(Price.minPrice(), Price::add);
    }

    public Long getId() {
        return id;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}
