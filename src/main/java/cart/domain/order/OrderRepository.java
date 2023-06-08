package cart.domain.order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Optional<Order> findOrderById(Long id);

    List<Order> findAllOrdersByMemberId(Long memberId);

    Long createOrder(Order order, Long pointId);
}
