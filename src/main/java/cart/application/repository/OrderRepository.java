package cart.application.repository;

import cart.domain.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository {

    void insert(Order order);
}
