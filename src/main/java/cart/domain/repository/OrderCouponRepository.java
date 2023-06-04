package cart.domain.repository;

import org.springframework.stereotype.Component;

@Component
public interface OrderCouponRepository {

    Long deleteByOrderId(Long orderId);
}
