package cart.repository;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.Order;
import cart.domain.value.Money;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public OrderRepository(final OrderDao orderDao, final OrderItemDao orderItemDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
    }

    public Order save(final Order order) {
        final OrderEntity orderEntity = orderDao.save(new OrderEntity(
                order.getId(),
                order.calculateTotalItemPrice(),
                order.calculateDiscountedTotalItemPrice(),
                order.getShippingFee(),
                order.getOrderedAt(),
                order.getMember().getId())
        );
        final List<OrderItemEntity> orderItemEntities = order.getOrderItems()
                .stream()
                .map(orderItem -> new OrderItemEntity(
                        orderItem.getId(),
                        orderItem.getName(),
                        orderItem.getPrice(),
                        orderItem.getImageUrl(),
                        orderItem.getQuantity(),
                        orderItem.getDiscountRate(),
                        orderEntity.getId())
                ).collect(Collectors.toList());
        orderItemDao.batchSave(orderItemEntities);
        return new Order(
                orderEntity.getId(),
                order.getOrderItems(),
                new Money(order.getShippingFee()),
                order.getOrderedAt(),
                order.getMember()
        );
    }
}
