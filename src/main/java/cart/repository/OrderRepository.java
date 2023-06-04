package cart.repository;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.VO.Money;
import cart.domain.coupon.Coupon;
import cart.domain.order.MemberCoupon;
import cart.domain.order.Order;
import cart.entity.CouponEntity;
import cart.entity.MemberCouponEntity;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import cart.exception.coupon.CouponNotFoundException;
import cart.exception.order.MemberCouponNotFoundException;
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
    private final MemberCouponDao memberCouponDao;

    public OrderRepository(
            final OrderDao orderDao,
            final CouponDao couponDao,
            final OrderItemDao orderItemDao,
            final MemberCouponDao memberCouponDao
    ) {
        this.orderDao = orderDao;
        this.couponDao = couponDao;
        this.orderItemDao = orderItemDao;
        this.memberCouponDao = memberCouponDao;
    }

    public Order save(final Order order) {
        final OrderEntity orderEntity = new OrderEntity(
                order.getDeliveryFee().getLongValue(),
                order.getMemberCoupon().getId(),
                order.getMemberId()
        );
        final OrderEntity savedOrderEntity = orderDao.insert(orderEntity);
        useMemberCoupon(order.getMemberCoupon());
        saveOrderItems(order, savedOrderEntity);
        return Order.of(
                savedOrderEntity.getId(),
                order.getMemberCoupon(),
                order.getMemberId(),
                order.getDeliveryFee(),
                order.getItems()
        );
    }

    private void useMemberCoupon(final MemberCoupon memberCoupon) {
        final MemberCouponEntity memberCouponEntity = MemberCouponEntity.from(memberCoupon);
        memberCouponDao.update(memberCouponEntity);
    }

    private void saveOrderItems(final Order order, final OrderEntity orderEntity) {
        final List<OrderItemEntity> orderItemEntities = order.getItems().stream()
                .map(cartItem -> OrderItemEntity.from(cartItem, orderEntity.getId()))
                .collect(toList());
        orderItemDao.insertAll(orderItemEntities);
    }

    public List<Order> findAllByMemberId(final Long memberId) {
        final Map<Long, MemberCoupon> couponIdByCoupon = getMemberCouponIdByMemberCoupon(memberId);
        final Map<Long, List<OrderItemEntity>> orderIdByOrderItems = getOrderIdByOrderItems(memberId);
        return orderDao.findAllByMemberId(memberId).stream()
                .map(orderEntity -> toOrder(orderEntity, couponIdByCoupon, orderIdByOrderItems))
                .collect(toList());
    }

    private Map<Long, MemberCoupon> getMemberCouponIdByMemberCoupon(final Long memberId) {
        final List<MemberCouponEntity> usedMemberCoupons = memberCouponDao.findAllByUsedAndMemberId(true, memberId);
        final List<Long> couponIds = usedMemberCoupons.stream()
                .map(MemberCouponEntity::getCouponId)
                .collect(toList());
        final Map<Long, Coupon> couponIdByCoupon = couponDao.findByIds(couponIds).stream()
                .map(CouponEntity::toDomain)
                .collect(toMap(Coupon::getId, Function.identity(), (coupon1, coupon2) -> coupon1));

        return usedMemberCoupons.stream()
                .map(it -> new MemberCoupon(it.getId(), memberId, couponIdByCoupon.get(it.getCouponId()), it.isUsed()))
                .collect(toMap(MemberCoupon::getId, Function.identity()));
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
            final Map<Long, MemberCoupon> memberCouponIdByMemberCoupon,
            final Map<Long, List<OrderItemEntity>> orderIdByOrderItems
    ) {
        final Long id = orderEntity.getId();
        return Order.of(
                id,
                memberCouponIdByMemberCoupon.get(orderEntity.getMemberCouponId()),
                orderEntity.getMemberId(),
                Money.from(orderEntity.getDeliveryFee()),
                orderIdByOrderItems.get(id).stream()
                        .map(OrderItemEntity::toDomain)
                        .collect(toList())
        );
    }

    public Optional<Order> findById(final Long id) {
        return orderDao.findById(id).map(orderEntity -> toOrder(orderEntity,
                getSingleMemberCouponIdByMemberCoupon(orderEntity),
                getSingleOrderIdByOrderItems(orderEntity)
        ));
    }

    private Map<Long, MemberCoupon> getSingleMemberCouponIdByMemberCoupon(final OrderEntity orderEntity) {
        if (orderEntity.getMemberCouponId() == 0) {
            return Collections.emptyMap();
        }
        final MemberCouponEntity memberCouponEntity = memberCouponDao.findById(orderEntity.getMemberCouponId())
                .orElseThrow(MemberCouponNotFoundException::new);
        final Coupon coupon = couponDao.findById(memberCouponEntity.getCouponId())
                .map(CouponEntity::toDomain)
                .orElseThrow(CouponNotFoundException::new);
        final MemberCoupon memberCoupon = new MemberCoupon(
                memberCouponEntity.getId(),
                memberCouponEntity.getMemberId(),
                coupon,
                memberCouponEntity.isUsed()
        );
        return Map.of(orderEntity.getMemberCouponId(), memberCoupon);
    }

    private Map<Long, List<OrderItemEntity>> getSingleOrderIdByOrderItems(final OrderEntity orderEntity) {
        return Map.of(orderEntity.getId(), orderItemDao.findAllByOrderId(orderEntity.getId()));
    }
}
