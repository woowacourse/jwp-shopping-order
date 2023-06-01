package cart.step2.order.domain.repository;

import cart.step2.order.domain.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository {

    Long save(final Order order);
}
