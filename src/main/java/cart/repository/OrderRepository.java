package cart.repository;

import cart.domain.Order;

import java.util.List;

public interface OrderRepository {

    Long save(Order order);

    List<Order> findByMemberId(Long memberId);

    Order findById(Long orderId);
}
