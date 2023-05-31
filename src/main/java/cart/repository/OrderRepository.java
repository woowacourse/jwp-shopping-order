package cart.repository;

import static java.util.stream.Collectors.toList;

import cart.dao.CartItemDao;
import cart.dao.CouponDao;
import cart.dao.OrderCouponDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.entity.OrderCouponEntity;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final OrderCouponDao orderCouponDao;
    private final CouponDao couponDao;
    private final CartItemDao cartItemDao;

    public OrderRepository(OrderDao orderDao, OrderItemDao orderItemDao, OrderCouponDao orderCouponDao,
                           CouponDao couponDao,
                           CartItemDao cartItemDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.orderCouponDao = orderCouponDao;
        this.couponDao = couponDao;
        this.cartItemDao = cartItemDao;
    }

    public Long save(Order order) {
        Long orderId = orderDao.save(OrderEntity.from(order));
        List<OrderItemEntity> orderItemEntities = order.getOrderItems().stream()
                .map(orderItem -> OrderItemEntity.from(orderItem, orderId))
                .collect(toList());
        orderItemDao.batchSave(orderItemEntities);
        List<OrderCouponEntity> orderCouponEntities = order.getOrderItems().stream()
                .flatMap(OrderRepository::createOrderCoupon)
                .collect(toList());
        orderCouponDao.batchSave(orderCouponEntities);
        return orderId;
    }

    private static Stream<OrderCouponEntity> createOrderCoupon(OrderItem orderItem) {
        return orderItem.getCoupons().stream()
                .map(coupon -> new OrderCouponEntity(orderItem.getId(), coupon.getId()));
    }
}
