package cart.repository;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
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

    public Order saveOrder(final Order order) {
        final OrderEntity orderEntity = orderDao.save(new OrderEntity(
                order.getId(),
                order.calculateTotalItemPrice(),
                order.calculateDiscountedTotalItemPrice(),
                order.getShippingFee(),
                order.getOrderedAt(),
                order.getMember().getId())
        );
        final List<OrderItemEntity> orderItemEntities = getOrderItemEntities(order, orderEntity);
        orderItemDao.batchSave(orderItemEntities);
        return new Order(
                orderEntity.getId(),
                order.getOrderItems(),
                order.getShippingFee(),
                order.getOrderedAt(),
                order.getMember()
        );
    }

    public Order findOrderById(final Member member, final Long orderId, final List<OrderItem> orderItems) {
        final OrderEntity orderEntity = orderDao.findById(orderId);
        return new Order(
                orderEntity.getId(),
                orderItems,
                orderEntity.getShippingFee(),
                orderEntity.getOrderedAt(),
                member
        );
    }

    public List<OrderItem> findAllOrderItemsByIdAndMemberId(final Long orderId, final Long memberId) {
        return orderItemDao.findAllOrderItemsByOrderIdAndMemberId(orderId, memberId)
                .stream()
                .map(orderItemEntity -> new OrderItem(
                        orderItemEntity.getId(),
                        orderItemEntity.getName(),
                        orderItemEntity.getPrice(),
                        orderItemEntity.getImageUrl(),
                        orderItemEntity.getQuantity(),
                        orderItemEntity.getDiscountRate()
                )).collect(Collectors.toList());
    }

    private List<OrderItemEntity> getOrderItemEntities(final Order order, final OrderEntity orderEntity) {
        return order.getOrderItems()
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
    }
}
