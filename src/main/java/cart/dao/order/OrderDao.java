package cart.dao.order;

import cart.domain.order.Order;

import java.util.List;
import java.util.Optional;

public interface OrderDao {

    Optional<Order> findOrderById(Long id);

    List<Order> findAllOrdersByMemberId(Long memberId);

    Long createOrder(Order order, Long pointId);
}
