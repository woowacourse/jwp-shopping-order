package cart.domain.order;

import cart.domain.order.Order;

import java.util.List;

public interface OrderRepository {
    List<Order> findOrderByMemberId(Long memberId);

    Order findOrderById(Long Id);

    Long add(Order order);

    void delete(Long id);

    Long update(Order order);
}
