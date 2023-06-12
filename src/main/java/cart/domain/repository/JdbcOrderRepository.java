package cart.domain.repository;

import cart.dao.OrderDao;
import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderProductEntity;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.domain.order.OrderProducts;
import cart.domain.payment.Payment;
import cart.exception.OrderException;
import cart.exception.OrderProductException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Repository
public class JdbcOrderRepository implements OrderRepository {

    private final OrderDao orderDao;

    public JdbcOrderRepository(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public long createOrder(Order order) {
        OrderProducts orderProducts = new OrderProducts(order.getOrderProducts());
        OrderEntity orderEntity = toOrderEntity(order);
        long orderId = orderDao.insertOrder(orderEntity);
        saveOrderItems(orderProducts, orderId);
        return orderId;
    }

    private OrderEntity toOrderEntity(Order order) {
        return new OrderEntity.Builder()
                .memberId(order.getMember().getId())
                .totalPayment(order.getTotalPriceValue())
                .usedPoint(order.getUsedPointValue())
                .build();
    }

    private void saveOrderItems(OrderProducts orderProducts, long orderId) {
        orderProducts.getOrderProducts()
                .stream()
                .map(orderProduct -> toOrderItemEntity(orderId, orderProduct))
                .forEach(orderDao::insertOrderItems);
    }

    private OrderProductEntity toOrderItemEntity(long orderId, OrderProduct orderProduct) {
        return new OrderProductEntity.Builder()
                .productId(orderProduct.getProductId())
                .orderId(orderId)
                .productName(orderProduct.getName())
                .productPrice(orderProduct.getPrice())
                .productImageUrl(orderProduct.getImageUrl())
                .quantity(orderProduct.getQuantity())
                .totalPrice(orderProduct.getTotalPrice())
                .build();
    }

    // 사용자별 주문 내역
    @Override
    public Optional<List<Order>> findOrderProductsByMemberId(Member member) {
        try {
            List<Long> orderIds = orderDao.getOrderIdsByMemberId(member.getId())
                    .orElseThrow(OrderProductException.NotFound::new);
            List<Order> orders = orderIds.stream()
                    .map(orderId -> findOrderById(member, orderId).orElseThrow(OrderException.NotFound::new))
                    .collect(toList());
            return Optional.of(orders);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Order> findOrderById(Member member, long orderId) {
        try {
            OrderEntity orderEntity = orderDao.getOrderById(orderId)
                    .orElseThrow(OrderException.NotFound::new);
            List<OrderProductEntity> orderProductEntities = orderDao.getOrderProductsByOrderId(orderId)
                    .orElseThrow(OrderProductException.NotFound::new);
            OrderProducts orderProducts = toOrderProducts(orderId, orderProductEntities);
            Payment payment = Payment.of(orderEntity.getTotalPayment(), orderEntity.getUsedPoint());
            Order order = Order.of(orderId, member, orderProducts, payment, orderEntity.getCreatedAt());
            return Optional.of(order);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    private static OrderProducts toOrderProducts(long orderId, List<OrderProductEntity> orderProductEntities) {
        return orderProductEntities.stream()
                .map(orderProductEntity -> new OrderProduct.Builder()
                        .id(orderProductEntity.getId())
                        .orderId(orderProductEntity.getOrderId())
                        .productId(orderProductEntity.getProductId())
                        .productName(orderProductEntity.getProductName())
                        .productPrice(orderProductEntity.getProductPrice())
                        .productImageUrl(orderProductEntity.getProductImageUrl())
                        .quantity(orderProductEntity.getQuantity())
                        .totalPrice(orderProductEntity.getTotalPrice())
                        .build()
                ).collect(collectingAndThen(toList(), (orderProducts) -> new OrderProducts(orderId, orderProducts)));
    }
}
