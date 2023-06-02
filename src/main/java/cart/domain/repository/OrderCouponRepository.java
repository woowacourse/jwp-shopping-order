package cart.domain.repository;

import cart.domain.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderCouponRepository {
    void saveOrderCoupon(Long orderId, Order order);

    Long deleteOrderCoupon(Long orderId);
}
