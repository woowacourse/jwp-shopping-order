package cart.repository;

import static cart.domain.coupon.Coupon.EMPTY;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import cart.dao.CartItemDao;
import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.Item;
import cart.domain.Order;
import cart.domain.common.Money;
import cart.domain.coupon.Coupon;
import cart.entity.CouponEntity;
import cart.entity.MemberCouponEntity;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final CouponDao couponDao;
    private final CartItemDao cartItemDao;
    private final OrderItemDao orderItemDao;
    private final MemberCouponDao memberCouponDao;

    public OrderRepository(final OrderDao orderDao, final CouponDao couponDao, final CartItemDao cartItemDao,
                           final OrderItemDao orderItemDao,
                           final MemberCouponDao memberCouponDao) {
        this.orderDao = orderDao;
        this.couponDao = couponDao;
        this.cartItemDao = cartItemDao;
        this.orderItemDao = orderItemDao;
        this.memberCouponDao = memberCouponDao;
    }

    public Order save(final Order order) {
        final OrderEntity orderEntity = new OrderEntity(
                order.getDeliveryFee().getLongValue(),
                order.getCoupon().getId(),
                order.getMemberId()
        );
        final OrderEntity savedOrderEntity = orderDao.insert(orderEntity);
        useMemberCoupon(savedOrderEntity);
        saveOrderItems(order, savedOrderEntity);
        deleteCartItems(order);
        return new Order(
                savedOrderEntity.getId(),
                order.getCoupon(),
                order.getMemberId(),
                order.getDeliveryFee(),
                order.getItems()
        );
    }

    private void useMemberCoupon(final OrderEntity orderEntity) {
        final MemberCouponEntity memberCouponEntity = new MemberCouponEntity(
                orderEntity.getCouponId(),
                orderEntity.getMemberId(),
                true
        );
        memberCouponDao.update(memberCouponEntity);
    }

    private void saveOrderItems(final Order order, final OrderEntity orderEntity) {
        final List<OrderItemEntity> orderItemEntities = order.getItems().stream()
                .map(cartItem -> OrderItemEntity.from(cartItem, orderEntity.getId()))
                .collect(toList());
        orderItemDao.insertAll(orderItemEntities);
    }

    private void deleteCartItems(final Order order) {
        final List<Long> cartItemIds = order.getItems().stream()
                .map(Item::getId)
                .collect(toList());
        cartItemDao.deleteByIds(cartItemIds, order.getMemberId());
    }

    public List<Order> findAllByMemberId(final Long memberId) {
        final Map<Long, Coupon> couponIdByCoupon = getCouponIdByCoupon(memberId);
        final Map<Long, List<OrderItemEntity>> orderIdByOrderItems = getOrderIdByOrderItems(memberId);
        return orderDao.findAllByMemberId(memberId).stream()
                .map(orderEntity -> toOrder(orderEntity, couponIdByCoupon, orderIdByOrderItems))
                .collect(toList());
    }

    private Map<Long, Coupon> getCouponIdByCoupon(final Long memberId) {
        final List<Long> couponIds = orderDao.findAllByMemberId(memberId).stream()
                .map(OrderEntity::getCouponId)
                .collect(toList());
        return couponDao.findByIds(couponIds).stream()
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
        return new Order(
                id,
                couponIdByCoupon.get(id),
                orderEntity.getMemberId(),
                Money.from(orderEntity.getDeliveryFee()),
                orderIdByOrderItems.get(id).stream()
                        .map(OrderItemEntity::toDomain)
                        .collect(toList())
        );
    }

    public Optional<Order> findById(final Long id, final Long memberId) {
        return orderDao.findById(id)
                .map(orderEntity -> toOrder(orderEntity,
                        getSingleCouponIdByCoupon(orderEntity),
                        getSingleOrderIdByOrderItems(orderEntity)
                ));
    }

    private Map<Long, Coupon> getSingleCouponIdByCoupon(final OrderEntity orderEntity) {
        final Coupon coupon = couponDao.findById(orderEntity.getCouponId())
                .map(CouponEntity::toDomain)
                .orElse(EMPTY);
        return Map.of(orderEntity.getCouponId(), coupon);
    }

    private Map<Long, List<OrderItemEntity>> getSingleOrderIdByOrderItems(final OrderEntity orderEntity) {
        return Map.of(orderEntity.getId(), orderItemDao.findAllByOrderId(orderEntity.getId()));
    }
}
