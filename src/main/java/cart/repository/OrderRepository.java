package cart.repository;

import cart.domain.order.Order;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import cart.repository.dao.OrderDao;
import cart.repository.dao.OrderItemDao;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public OrderRepository(final OrderDao orderDao, final OrderItemDao orderItemDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
    }

    public Long createOrder(Order order) {
        final OrderEntity orderEntity = OrderEntity.from(order);
        final Long orderId = orderDao.createOrder(orderEntity);

        final List<OrderItemEntity> orderItems = OrderItemEntity.from(orderId, order.getOrderItems());
        orderItemDao.saveAll(orderItems);

        return orderId;
    }
}
