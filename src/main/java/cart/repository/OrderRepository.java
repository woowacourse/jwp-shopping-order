package cart.repository;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderItemEntity;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Order;
import cart.exception.CouponException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    public long add(final Member member, final Order order) {
        final long orderId = orderDao.save(OrderEntity.from(order));
        final Long couponId = order.getCouponId();
        if (Objects.nonNull(couponId)) {
            final Coupon coupon = couponRepository.findById(couponId)
                    .orElseThrow(() -> new CouponException.IllegalId(couponId));
            couponRepository.updateStatus(coupon.use(member));
        }
        orderItemDao.saveAll(OrderItemEntity.of(orderId, order.getOrderItems()));
        return orderId;
    }

    public List<Order> findByMember(final Member member) {
        final List<OrderEntity> foundOrders = orderDao.findByMemberId(member.getId());
        final List<Order> orders = new ArrayList<>();
        for (final OrderEntity foundOrder : foundOrders) {
            final List<OrderItemEntity> foundOrderItems = orderItemDao.findByOrderId(foundOrder.getId());
            final Long couponId = foundOrder.getCouponId();
            orders.add(createOrder(foundOrder, foundOrderItems, couponId));
        }
        return orders;
    }

    private Order createOrder(final OrderEntity foundOrder,
                              final List<OrderItemEntity> foundOrderItems,
                              final Long couponId) {
        if (Objects.nonNull(couponId)) {
            final Coupon foundCoupon = couponRepository.findById(couponId)
                    .orElseThrow(() -> new IllegalStateException("illegal data exists in table ORDERS; coupon_id"));
            return Order.of(foundOrder, foundOrderItems, foundCoupon);
        }
        return Order.of(foundOrder, foundOrderItems);
    }

    public Optional<Order> findById(final Long orderId) {
        final Optional<OrderEntity> foundResult = orderDao.findById(orderId);
        if (foundResult.isEmpty()) {
            return Optional.empty();
        }
        final OrderEntity foundOrder = foundResult.get();
        final List<OrderItemEntity> foundOrderItems = orderItemDao.findByOrderId(orderId);
        final Long couponId = foundOrder.getCouponId();
        if (Objects.nonNull(couponId)) {
            final Coupon foundCoupon = couponRepository.findById(couponId)
                    .orElseThrow(() -> new IllegalStateException("illegal data exists in table ORDERS; coupon_id"));
            return Optional.of(Order.of(foundOrder, foundOrderItems, foundCoupon));
        }
        return Optional.of(Order.of(foundOrder, foundOrderItems));
    }

    public void remove(final Long orderId) {
        orderDao.deleteById(orderId);
        orderItemDao.deleteByOrderId(orderId);
    }

    public void update(final Order order) {
        final Coupon coupon = order.getCoupon();
        if (Objects.nonNull(coupon)) {
            couponRepository.updateStatus(coupon);
        }
        orderDao.updateStatus(OrderEntity.from(order));
    }
}
