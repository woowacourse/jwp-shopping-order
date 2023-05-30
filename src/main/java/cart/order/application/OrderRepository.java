package cart.order.application;

import cart.order.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {
    private final Map<Long, Order> orders = new HashMap<>();
    private long id = 1L;

    public Long save(Order order) {
        final var id = this.id++;
        this.orders.put(id, order.assignId(id));
        return id;
    }

    public Order findById(long id) {
        return this.orders.get(id);
    }

    public List<Order> findAllByMemberId(Long memberId) {
        return orders.values()
                .stream().filter(order -> order.getMemberId().equals(memberId))
                .collect(Collectors.toList());
    }
}
