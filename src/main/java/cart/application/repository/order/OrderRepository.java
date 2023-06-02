package cart.application.repository.order;

import cart.domain.order.Order;

import java.util.List;

public interface OrderRepository {

    Long createOrder(Order order);

    List<Order> findAllByMemberId(Long id);
}
