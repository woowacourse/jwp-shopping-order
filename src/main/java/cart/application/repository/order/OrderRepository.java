package cart.application.repository.order;

import cart.domain.order.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Long createOrder(Order order);

    List<Order> findOrdersByMemberId(Long memberId);

    Optional<Order> findOrderBy(long orderId);

}
