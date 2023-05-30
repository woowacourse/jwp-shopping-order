package cart.repository;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.entity.OrderEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public OrderRepository(final OrderDao orderDao, final OrderItemDao orderItemDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
    }

    public long save(final Order order){
        OrderEntity orderEntity = order.toEntity();
        long orderId = orderDao.save(orderEntity);
        for(OrderItem orderItem : order.getOrderItems()){
            orderItemDao.insert(orderId, orderItem);
        }
        return orderId;
    }
}
