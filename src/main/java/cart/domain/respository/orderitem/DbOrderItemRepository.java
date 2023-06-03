package cart.domain.respository.orderitem;

import cart.dao.OrderItemDao;
import cart.domain.order.OrderItem;
import org.springframework.stereotype.Repository;

@Repository
public class DbOrderItemRepository implements OrderItemRepository {

    private final OrderItemDao orderItemDao;

    public DbOrderItemRepository(final OrderItemDao orderItemDao) {
        this.orderItemDao = orderItemDao;
    }

    @Override
    public OrderItem insert(final Long orderId, final OrderItem orderItem) {
        return orderItemDao.insert(orderId, orderItem);
    }
}
