package cart.repository;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.OrderItems;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderJdbcRepository implements OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public OrderJdbcRepository(final OrderDao orderDao, final OrderItemDao orderItemDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
    }

    @Override
    public long save(final Order order) {
        final long orderId = orderDao.insert(toOrderEntity(order));
        orderItemDao.insertAll(mapToOrderItemEntities(orderId, order.getOrderItems()));
        return orderId;
    }

    private OrderEntity toOrderEntity(final Order order) {
        return new OrderEntity(order.getMember().getId(), order.getOriginalPrice(), order.getDiscountPrice());
    }

    private List<OrderItemEntity> mapToOrderItemEntities(final long orderId, final OrderItems orderItems) {
        return orderItems.getOrderItems().stream()
                .map(orderItem -> toOrderItemEntity(orderId, orderItem))
                .collect(Collectors.toUnmodifiableList());
    }

    private OrderItemEntity toOrderItemEntity(final long orderId, final OrderItem orderItem) {
        return new OrderItemEntity(orderId, orderItem.getProduct().getId(), orderItem.getQuantity().getValue());
    }
}
