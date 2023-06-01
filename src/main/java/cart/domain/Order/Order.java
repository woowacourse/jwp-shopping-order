package cart.domain.Order;

import java.sql.Timestamp;
import java.util.List;

public class Order {
    private final Long id;

    private final Timestamp orderDate;

    List<OrderItem> orderItem;

    public Order(Long id, Timestamp orderDate, List<OrderItem> orderItem) {
        this.id = id;
        this.orderDate = orderDate;
        this.orderItem = orderItem;
    }

    public Order(List<OrderItem> orderItem) {
        this(null, null, orderItem);
    }

    public List<OrderItem> getOrderItem() {
        return orderItem;
    }

    public int getTotalPrice() {
        return orderItem.stream()
                .mapToInt(orderItem -> orderItem.getPrice() * orderItem.getQuantity())
                .sum();
    }

    public Long getId() {
        return id;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }
}
