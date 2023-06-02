package cart.persistence;

import cart.application.repository.OrderRepository;
import cart.domain.order.Order;
import cart.persistence.dao.OrderDao;
import cart.persistence.dao.OrderItemDao;
import cart.persistence.entity.OrderEntity;
import cart.persistence.entity.OrderItemEntity;
import org.springframework.stereotype.Repository;

@Repository
public class OrderJdbcRepository implements OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public OrderJdbcRepository(final OrderDao orderDao, final OrderItemDao orderItemDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
    }

    @Override
    public long order(final Order order) {
        OrderEntity entity = OrderEntity.from(order);
        long orderId = orderDao.create(entity);
        orderItemDao.createAll(OrderItemEntity.from(order));
        return orderId;
    }
}
