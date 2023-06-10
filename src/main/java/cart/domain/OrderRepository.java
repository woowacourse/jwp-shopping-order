package cart.domain;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

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

    public Order findById(final Long id) {
        final OrderEntity orderEntity = orderDao.findById(id);
        final List<OrderItem> orderItems = orderItemDao.findByOrderId(id);

        return generateOrder(orderEntity, orderItems);
    }

    private Order generateOrder(final OrderEntity orderEntity, final List<OrderItem> orderItems) {
        return new Order(orderEntity.getId(),
                orderEntity.getMemberId(),
                orderItems,
                orderEntity.getTotalPrice(),
                orderEntity.getPayPrice(),
                orderEntity.getEarnedPoints(),
                orderEntity.getUsedPoints(),
                orderEntity.getOrderDate());
    }

    public List<Order> findPageByIndex(final Long memberId, final Long index) {
        final List<OrderEntity> orderEntities = orderDao.findByIndexRange(memberId, index);

        return orderEntities.stream().map(orderEntity -> {
            final List<OrderItem> orderItems = orderItemDao.findByOrderId(orderEntity.getId());
            return generateOrder(orderEntity, orderItems);
        }).collect(Collectors.toList());
    }
}
