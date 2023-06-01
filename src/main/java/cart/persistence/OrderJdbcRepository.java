package cart.persistence;

import cart.application.repository.OrderRepository;
import cart.domain.order.Order;
import cart.persistence.dao.OrderDao;
import cart.persistence.entity.OrderEntity;
import org.springframework.stereotype.Repository;

@Repository
public class OrderJdbcRepository implements OrderRepository {

    private final OrderDao orderDao;

    public OrderJdbcRepository(final OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public long order(final Order order) {
        OrderEntity entity = OrderEntity.from(order);
        return orderDao.create(entity);
    }
}
