package cart.application.repository;

import cart.domain.order.Order;
import java.util.Optional;

public interface OrderRepository {

    long order(Order order);

    Optional<Order> findById(long id);
}
