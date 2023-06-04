package cart.domain.repository;

import cart.domain.Order;
import org.springframework.stereotype.Component;

@Component
public interface OrderCouponRepository {
    void save(Long orderId, Order order);

    Long deleteByOrderId(Long orderId);
}
