package cart.repository;

import cart.domain.Order;

public interface OrderRepository {

    long save(final Order order);
}
