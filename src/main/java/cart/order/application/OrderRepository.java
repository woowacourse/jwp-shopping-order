package cart.order.application;

import cart.order.domain.Order;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Long save(long memberId);
    Optional<Order> findById(Long orderId);
    List<Order> findByMemberId(Long memberId);
}
