package cart.repository;

import cart.domain.Order;

public interface OrderRepository {

    Order save(final Order order);
}
