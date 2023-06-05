package cart.repository;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import cart.dao.CouponDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.VO.Money;
import cart.domain.coupon.Coupon;
import cart.domain.order.Order;
import cart.entity.CouponEntity;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import cart.exception.coupon.CouponNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final CouponDao couponDao;
    private final OrderItemDao orderItemDao;

    public OrderRepository(
            final OrderDao orderDao,
            final CouponDao couponDao,
            final OrderItemDao orderItemDao
    ) {
        this.orderDao = orderDao;
        this.couponDao = couponDao;
        this.orderItemDao = orderItemDao;
    }

    public Order save(final Order order) {
        final OrderEntity orderEntity = OrderEntity.from(order);
        final OrderEntity savedOrderEntity = orderDao.insert(orderEntity);
        saveOrderItems(order, savedOrderEntity);
        return Order.of(
                savedOrderEntity.getId(),
                order.getCoupon(),
                order.getMemberId(),
                order.getDeliveryFee(),
                order.getItems()
        );
    }

    private void saveOrderItems(final Order order, final OrderEntity orderEntity) {
        final List<OrderItemEntity> orderItemEntities = order.getItems().stream()
                .map(cartItem -> OrderItemEntity.from(cartItem, orderEntity.getId()))
                .collect(toList());
        orderItemDao.insertAll(orderItemEntities);
    }

    public List<Order> findAllByMemberId(final Long memberId) {
        final Map<Long, Coupon> couponIdByCoupon = getCouponIdByCoupon(memberId);
        final Map<Long, List<OrderItemEntity>> orderIdByOrderItems = getOrderIdByOrderItems(memberId);
        return orderDao.findAllByMemberId(memberId).stream()
                .map(orderEntity -> toOrder(orderEntity, couponIdByCoupon, orderIdByOrderItems))
                .collect(toList());
    }

    private Map<Long, Coupon> getCouponIdByCoupon(final Long memberId) {
        final List<CouponEntity> usedCoupons = couponDao.findAllByUsedAndMemberId(true, memberId);
        return usedCoupons.stream()
                .map(CouponEntity::toDomain)
                .collect(toMap(Coupon::getId, Function.identity()));
    }

    private Map<Long, List<OrderItemEntity>> getOrderIdByOrderItems(final Long memberId) {
        final List<Long> orderIds = orderDao.findAllByMemberId(memberId).stream()
                .map(OrderEntity::getId)
                .collect(toList());
        return orderItemDao.findAllByOrderIds(orderIds).stream()
                .collect(groupingBy(OrderItemEntity::getOrderId));
    }

    private Order toOrder(
            final OrderEntity orderEntity,
            final Map<Long, Coupon> couponIdByCoupon,
            final Map<Long, List<OrderItemEntity>> orderIdByOrderItems
    ) {
        final Long id = orderEntity.getId();
        return Order.of(
                id,
                couponIdByCoupon.get(orderEntity.getCouponId()),
                orderEntity.getMemberId(),
                Money.from(orderEntity.getDeliveryFee()),
                orderIdByOrderItems.get(id).stream()
                        .map(OrderItemEntity::toDomain)
                        .collect(toList())
        );
    }

    public Optional<Order> findById(final Long id) {
        return orderDao.findById(id).map(orderEntity -> toOrder(
                orderEntity,
                getSingleCouponIdByCoupon(orderEntity),
                getSingleOrderIdByOrderItems(orderEntity)
        ));
    }

    private Map<Long, Coupon> getSingleCouponIdByCoupon(final OrderEntity orderEntity) {
        if (orderEntity.getCouponId() == 0) {
            return Collections.emptyMap();
        }
        final CouponEntity couponEntity = couponDao.findById(orderEntity.getCouponId())
                .orElseThrow(CouponNotFoundException::new);
        return Map.of(orderEntity.getCouponId(), couponEntity.toDomain());
    }

    private Map<Long, List<OrderItemEntity>> getSingleOrderIdByOrderItems(final OrderEntity orderEntity) {
        return Map.of(orderEntity.getId(), orderItemDao.findAllByOrderId(orderEntity.getId()));
    }
}
