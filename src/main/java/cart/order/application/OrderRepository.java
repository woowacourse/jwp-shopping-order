package cart.order.application;

import cart.order.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderRepository {
    private final List<Order> orders = new ArrayList<>();
    private long id = 1L;

    public Long save(Order order) {
        final var id = this.id++;
        this.orders.add(order.assignId(id));
        return id;
    }
}
