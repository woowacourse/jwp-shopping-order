package cart.repository;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.Member;
import cart.domain.Money;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Product;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public OrderRepository(OrderDao orderDao, OrderItemDao orderItemDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
    }

    public Order save(Member member, Order order) {
        OrderEntity orderEntity = OrderEntity.of(member, order);
        Long orderId = orderDao.save(orderEntity);

        List<OrderItemEntity> orderItemEntities = order.getOrderItems()
                .stream()
                .map(orderItem -> OrderItemEntity.of(orderId, orderItem))
                .collect(Collectors.toList());

        orderItemDao.saveAll(orderItemEntities);

        return Order.of(
                orderId,
                order.getOrderItems(),
                member.getId(),
                order.getUsedPoints(),
                order.getEarnedPoints(),
                order.getOrderDate()
        );
    }

    public Optional<Order> findByOrderId(Long orderId) {
        return orderDao.findByOrderId(orderId)
                .map(this::mapToOrder);
    }

    public List<Order> findByMemberAndLastOrderIdAndSize(Member member, Long lastOrderId, int size) {
        List<OrderEntity> orderEntities = orderDao.findByMemberIdAndLastOrderIdAndSize(
                member.getId(),
                lastOrderId,
                size
        );

        return orderEntities.stream()
                .map(this::mapToOrder)
                .collect(Collectors.toList());
    }

    private Order mapToOrder(OrderEntity orderEntity) {
        List<OrderItem> orderItems = orderItemDao.findByOrderId(orderEntity.getId())
                .stream()
                .map(this::mapToOrderItem)
                .collect(Collectors.toList());

        Money earnedPoints = new Money(orderEntity.getEarnedPoints());
        Money usedPoints = new Money(orderEntity.getUsedPoints());

        return Order.of(
                orderEntity.getId(),
                orderItems,
                orderEntity.getMemberId(),
                usedPoints,
                earnedPoints,
                orderEntity.getOrderDate()
        );
    }

    private OrderItem mapToOrderItem(OrderItemEntity orderItemEntity) {
        Product product = new Product(
                orderItemEntity.getProductId(),
                orderItemEntity.getProductName(),
                orderItemEntity.getProductPrice(),
                orderItemEntity.getProductImageUrl()
        );

        return new OrderItem(orderItemEntity.getId(), product, orderItemEntity.getQuantity());
    }
}
