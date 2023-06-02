package cart.application.repository.order;

import cart.domain.order.Order;

public interface OrderRepository {

    Long createOrder(Order order);

}
