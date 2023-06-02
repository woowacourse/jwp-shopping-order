package cart.repository;

import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.OrderItems;
import cart.repository.dao.OrderDao;
import cart.repository.entity.OrderEntity;
import cart.repository.entity.OrderItemEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;

    public OrderRepository(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public long createOrder(Order order) {
        OrderItems orderItems = order.getOrderItems();
        OrderEntity orderEntity = toOrderEntity(order);
        long orderId = orderDao.insertOrder(orderEntity);
        saveOrderItems(orderItems, orderId);
        return orderId;
    }

    private void saveOrderItems(OrderItems orderItems, long orderId) {
        orderItems.getOrderItems()
                .stream()
                .map(orderItem -> toOrderItemEntity(orderId, orderItem))
                .forEach(orderDao::insertOrderItems);
    }

    private static OrderItemEntity toOrderItemEntity(long orderId, OrderItem orderItem) {
        return new OrderItemEntity.Builder()
                .orderId(orderId)
                .productName(orderItem.getName())
                .productPrice(orderItem.getPrice())
                .productImageUrl(orderItem.getImageUrl())
                .quantity(orderItem.getQuantity())
                .build();
    }

    private static OrderEntity toOrderEntity(Order order) {
        return new OrderEntity.Builder()
                .memberId(order.getMember().getId())
                .totalPayment(order.getPayment().getTotalPayment())
                .usedPoint(order.getPayment().getUsedPoint())
                .build();
    }

    // 사용자별 주문 내역
    public OrderItems findOrderItemsByMemberId(long memberId) {
        List<OrderItemEntity> orderEntities = orderDao.getOrdersByMemberId(memberId);
        long orderId = orderEntities.get(0).getOrderId();
        return orderEntities.stream()
                .map(orderItemEntity -> new OrderItem.Builder()
                        .id(orderItemEntity.getId())
                        .orderId(orderItemEntity.getOrderId())
                        .productName(orderItemEntity.getProductName())
                        .productPrice(orderItemEntity.getProductPrice())
                        .productImageUrl(orderItemEntity.getProductImageUrl())
                        .quantity(orderItemEntity.getQuantity())
                        .build()
                ).collect(collectingAndThen(toList(), (orderItems) -> new OrderItems(orderId, orderItems)));
    }
}
