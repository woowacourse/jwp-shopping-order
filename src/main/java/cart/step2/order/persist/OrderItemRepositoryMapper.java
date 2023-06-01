package cart.step2.order.persist;

import cart.step2.order.domain.OrderItem;
import cart.step2.order.domain.repository.OrderItemRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderItemRepositoryMapper implements OrderItemRepository {

    private final OrderItemDao orderItemDao;

    public OrderItemRepositoryMapper(final OrderItemDao orderItemDao) {
        this.orderItemDao = orderItemDao;
    }

    @Override
    public void createAllOrderItems(final List<OrderItem> orderItems) {
        orderItemDao.batchInsert(orderItems);
    }

}
