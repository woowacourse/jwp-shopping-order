package cart.repository;

import cart.domain.Order;

public interface OrderRepository {

    Long save(Order order);
}
