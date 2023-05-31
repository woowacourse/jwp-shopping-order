package cart.repository;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderItemEntity;
import cart.domain.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Repository
public class OrderJdbcRepository implements OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public OrderJdbcRepository(OrderDao orderDao, OrderItemDao orderItemDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
    }

    @Override
    public Long save(final Order order) {
        final OrderEntity orderEntity = new OrderEntity(order.getPrice(), order.getCoupon().getId(), order.getMember().getId());
        final Long orderId = orderDao.save(orderEntity);

        final List<OrderItemEntity> orderItemEntities = order.getOrderItems().stream()
                .map(orderItem -> new OrderItemEntity(orderItem.getProduct().getId(), orderId, orderItem.getQuantity()))
                .collect(toList());

        orderItemDao.saveAll(orderItemEntities);
        return orderId;
    }
}
