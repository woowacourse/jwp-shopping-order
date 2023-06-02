package cart.repository;

import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderProduct;
import cart.domain.OrderProducts;
import cart.domain.Payment;
import cart.repository.dao.OrderDao;
import cart.repository.entity.OrderEntity;
import cart.repository.entity.OrderProductEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
        OrderProducts orderProducts = order.getOrderItems();
        OrderEntity orderEntity = toOrderEntity(order);
        long orderId = orderDao.insertOrder(orderEntity);
        saveOrderItems(orderProducts, orderId);
        return orderId;
    }

    private void saveOrderItems(OrderProducts orderProducts, long orderId) {
        orderProducts.getOrderItems()
                .stream()
                .map(orderItem -> toOrderItemEntity(orderId, orderItem))
                .forEach(orderDao::insertOrderItems);
    }

    private static OrderProductEntity toOrderItemEntity(long orderId, OrderProduct orderProduct) {
        return new OrderProductEntity.Builder()
                .orderId(orderId)
                .productName(orderProduct.getName())
                .productPrice(orderProduct.getPrice())
                .productImageUrl(orderProduct.getImageUrl())
                .quantity(orderProduct.getQuantity())
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
    public List<OrderProducts> findOrderItemsByMemberId(long memberId) {
        List<Long> orderIds = orderDao.getOrderIdsByMemberId(memberId);
        List<OrderProducts> orders = new ArrayList<>();
        for (long orderId : orderIds) {
            List<OrderProductEntity> orderItemEntities = orderDao.getOrderItemsByOrderId(orderId);
            OrderProducts orderProducts = toOrderItems(orderId, orderItemEntities);
            orders.add(orderProducts);
        }
        return orders;
    }

    private static OrderProducts toOrderItems(long orderId, List<OrderProductEntity> orderItemEntities) {
        return orderItemEntities.stream()
                .map(orderProductEntity -> new OrderProduct.Builder()
                        .id(orderProductEntity.getId())
                        .orderId(orderProductEntity.getOrderId())
                        .productName(orderProductEntity.getProductName())
                        .productPrice(orderProductEntity.getProductPrice())
                        .productImageUrl(orderProductEntity.getProductImageUrl())
                        .quantity(orderProductEntity.getQuantity())
                        .build()
                ).collect(collectingAndThen(toList(), (items) -> new OrderProducts(orderId, items)));
    }

    // 주문 상세
    public Order findOrderById(Member member, long orderId) {
        OrderEntity orderEntity = orderDao.getOrderById(orderId);
        OrderProducts orderProducts = toOrderItems(orderId, orderDao.getOrderItemsByOrderId(orderId));
        Payment payment = new Payment(orderEntity.getTotalPayment(), orderEntity.getUsedPoint());
        return new Order(orderId, member, orderProducts, payment, orderEntity.getCreatedAt());
    }
}
