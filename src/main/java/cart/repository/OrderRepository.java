package cart.repository;

import cart.domain.Order;

import java.util.List;

public interface OrderRepository {

    Long save(Long memberId, Order order);

    void delete(Long memberId, Long orderId);

    List<Order> findAllByMemberId(Long memberId);

    Order findOrder(Long memberId, Long orderId);
}
