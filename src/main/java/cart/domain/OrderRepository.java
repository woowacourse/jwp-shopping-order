package cart.domain;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public OrderRepository(final OrderDao orderDao, final OrderItemDao orderItemDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
    }

    public Long saveOrder(final Order order) {
        final Long orderId = orderDao.insert(order);

        order.getOrderItems().forEach(orderItem -> orderItemDao.insert(orderId, orderItem));

        return orderId;
    }
}
