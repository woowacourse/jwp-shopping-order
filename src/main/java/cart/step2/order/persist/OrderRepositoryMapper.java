package cart.step2.order.persist;

import cart.step2.order.domain.Order;
import cart.step2.order.domain.OrderEntity;
import cart.step2.order.domain.repository.OrderRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryMapper implements OrderRepository {

    private final OrderDao orderDao;

    public OrderRepositoryMapper(final OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public Long save(final Order order) {
        OrderEntity orderEntity = OrderEntity.createNonePkOrder(order.getPrice(), order.getCouponId(), order.getMemberId());
        return orderDao.insert(orderEntity);
    }

}
