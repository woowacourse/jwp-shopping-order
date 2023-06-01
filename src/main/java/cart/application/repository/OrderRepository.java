package cart.application.repository;

import cart.domain.order.Order;

public interface OrderRepository {

    long order(final Order order);
}
