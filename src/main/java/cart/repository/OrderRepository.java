package cart.repository;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderItemEntity;
import cart.domain.Coupon;
import cart.domain.Order;
import cart.domain.OrderItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final CouponRepository couponRepository;

    public OrderRepository(final OrderDao orderDao,
                           final OrderItemDao orderItemDao,
                           final CouponRepository couponRepository) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.couponRepository = couponRepository;
    }

    public long add(final long memberId, final Order order) {
        final long orderId = orderDao.save(OrderEntity.from(memberId, order));
        if (order.hasCoupon()) {
            couponRepository.updateStatus(memberId, order.getCoupon());
        }
        orderItemDao.saveAll(OrderItemEntity.of(orderId, order.getOrderItems()));
        return orderId;
    }

    public List<Order> findByMember(final long memberId) {
        final List<OrderEntity> foundOrders = orderDao.findByMemberId(memberId);
        final List<Order> orders = new ArrayList<>();
        for (final OrderEntity foundOrder : foundOrders) {
            final List<OrderItemEntity> foundOrderItems = orderItemDao.findByOrderId(foundOrder.getId());
            orders.add(createOrder(foundOrder, foundOrderItems));
        }
        return orders;
    }

    private Order createOrder(final OrderEntity foundOrder,
                              final List<OrderItemEntity> foundOrderItems) {
        final List<OrderItem> orderItems = OrderItemEntity.createAll(foundOrderItems);
        final Coupon coupon = foundOrder.getOptionalCouponId()
                .flatMap(couponRepository::findById)
                .orElse(null);
        return foundOrder.create(orderItems, coupon);
    }

    public Optional<Order> findByIdForMember(final long memberId, final long orderId) {
        return orderDao.findByIdForMember(memberId, orderId)
                .map(orderEntity -> createOrder(orderEntity, orderItemDao.findByOrderId(orderId)));
    }

    public void remove(final long orderId) {
        orderDao.deleteById(orderId);
        orderItemDao.deleteByOrderId(orderId);
    }

    public void update(final long memberId, final Order order) {
        if (order.hasCoupon()) {
            couponRepository.updateStatus(memberId, order.getCoupon());
        }
        orderDao.updateStatus(OrderEntity.from(memberId, order));
    }
}
