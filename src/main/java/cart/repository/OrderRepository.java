package cart.repository;

import cart.domain.Order;

import java.util.List;

public interface OrderRepository {

    Long save(final Order order);

    List<Order> findOrderByMemberId(final Long id);
}
