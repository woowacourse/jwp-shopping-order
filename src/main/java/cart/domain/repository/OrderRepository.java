package cart.domain.repository;

import cart.domain.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository {
    Long saveOrder(Order order);
}
