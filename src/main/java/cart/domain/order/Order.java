package cart.domain.order;

import cart.domain.product.Price;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {

    private final Long id;
    private final Timestamp orderTime;
    private final List<OrderItem> orderItems;

    public Order(final List<OrderItem> orderItemList) {
        this(null, null, orderItemList);
    }

    public Order(
            final Long id,
            final Timestamp orderTime,
            final List<OrderItem> orderItemList
    ) {
        this.id = id;
        this.orderTime = orderTime;
        this.orderItems = new ArrayList<>(orderItemList);
    }

    public Price getTotalPrice() {
        return orderItems.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(Price.minPrice(), Price::add);
    }

    public Long getId() {
        return id;
    }

    public Timestamp getOrderTime() {
        return orderTime;
    }

    public List<OrderItem> getOrderItems() {
        return Collections.unmodifiableList(orderItems);
    }
}
