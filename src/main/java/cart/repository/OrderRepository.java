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

@Repository
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

    public long add(final Order order) {
        final long orderId = orderDao.save(OrderEntity.from(order));
        // TODO 쿠폰 상태 변경 (null이면 변경 x)
        orderItemDao.saveAll(OrderItemEntity.of(orderId, order.getOrderItems()));
        return orderId;
    }

    public List<Order> findByMember(final Member member) {
        final List<OrderEntity> foundOrders = orderDao.findByMemberId(member.getId());
        final List<Order> orders = new ArrayList<>();
        for (final OrderEntity foundOrder : foundOrders) {
            final List<OrderItemEntity> foundOrderItems = orderItemDao.findByOrderId(foundOrder.getId());
            orders.add(Order.of(foundOrder, foundOrderItems));
        }
        return orders;
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
                    .orElseThrow(() -> new CouponException.IllegalId(couponId));
            return Optional.of(Order.of(foundOrder, foundOrderItems, foundCoupon));
        }
        return Optional.of(Order.of(foundOrder, foundOrderItems));
    }

    public void remove(final Long orderId) {
        orderDao.deleteById(orderId);
        orderItemDao.deleteByOrderId(orderId);
    }

    public void update(final Order order) {
        // TODO 쿠폰 상태 변경 (null이면 변경 x)
        orderDao.updateStatus(OrderEntity.from(order));
    }
}
