package cart.repository;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.dao.MemberDao;
import cart.dao.OrderItemDao;
import cart.dao.OrdersDao;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.MemberCoupon;
import cart.domain.coupon.policy.DiscountPolicy;
import cart.domain.coupon.policy.DiscountPolicyType;
import cart.entity.CouponEntity;
import cart.entity.MemberCouponEntity;
import cart.entity.OrderItemEntity;
import cart.entity.OrdersEntity;
import cart.exception.CouponNotFoundException;
import cart.exception.MemberNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrdersDao ordersDao;
    private final OrderItemDao orderItemDao;
    private final MemberDao memberDao;
    private final CouponDao couponDao;
    private final MemberCouponDao memberCouponDao;

    public OrderRepository(final OrdersDao ordersDao, final OrderItemDao orderItemDao, final MemberDao memberDao,
                           final CouponDao couponDao, final MemberCouponDao memberCouponDao) {
        this.ordersDao = ordersDao;
        this.orderItemDao = orderItemDao;
        this.memberDao = memberDao;
        this.couponDao = couponDao;
        this.memberCouponDao = memberCouponDao;
    }

    public Order save(final Order order) {
        final OrdersEntity ordersEntity = new OrdersEntity(
                order.getDeliveryFee(),
                order.getMemberCoupon().getId(),
                order.getMember().getId());

        final OrdersEntity savedOrders = ordersDao.insert(ordersEntity);
        final Long savedOrderId = savedOrders.getId();

        List<OrderItem> savedOrderItems = new ArrayList<>();
        for (OrderItem orderItem : order.getOrderItems()) {
            final OrderItemEntity orderItemEntity = new OrderItemEntity(
                    orderItem.getName(),
                    orderItem.getPrice(),
                    orderItem.getImageUrl(),
                    orderItem.getQuantity(),
                    savedOrderId);
            final OrderItemEntity savedOrderItem = orderItemDao.insert(orderItemEntity);
            savedOrderItems.add(new OrderItem(
                    savedOrderItem.getId(),
                    savedOrderItem.getName(),
                    savedOrderItem.getPrice(),
                    savedOrderItem.getImageUrl(),
                    savedOrderItem.getQuantity()
            ));
        }

        return new Order(
                savedOrderId,
                order.getMemberCoupon(),
                order.getMember(),
                savedOrderItems
        );
    }

    public Order findByOrderIdAndMemberId(final Long orderId, final Long memberId) {
        final OrdersEntity ordersEntity = ordersDao.findByOrderIdAndMemberId(orderId, memberId);

        final List<OrderItem> orderItems = makeOrderItems(orderItemDao.findAllByOrderId(orderId));
        final Member member = getMember(memberId);
        final MemberCoupon memberCoupon = getMemberCoupon(member, ordersEntity.getMemberCouponId());

        return new Order(
                orderId,
                memberCoupon,
                member,
                orderItems
        );
    }

    private MemberCoupon getMemberCoupon(final Member member, final Long memberCouponId) {
        Optional<MemberCouponEntity> findMemberCoupon = memberCouponDao.findMemberCouponByMemberIdAndCouponId(
                member.getId(), memberCouponId);

        if (findMemberCoupon.isEmpty()) {
            return MemberCoupon.makeNonMemberCoupon();
        }
        Coupon coupon = getCoupon(findMemberCoupon.get().getCouponId());
        return new MemberCoupon(findMemberCoupon.get().getId(), member, coupon, findMemberCoupon.get().getUsed());
    }

    private List<OrderItem> makeOrderItems(final List<OrderItemEntity> orderItemEntities) {
        return orderItemEntities.stream()
                .map(it -> new OrderItem(it.getId(), it.getName(), it.getPrice(), it.getImageUrl(), it.getQuantity()))
                .collect(Collectors.toList());
    }

    public List<Order> findAllByMemberId(final Long memberId) {
        List<OrdersEntity> findOrderEntities = ordersDao.findByMemberId(memberId);
        return findOrderEntities.stream()
                .map(this::makeOrder)
                .collect(Collectors.toList());
    }

    private Order makeOrder(final OrdersEntity ordersEntity) {
        final Long orderEntityId = ordersEntity.getId();
        final Member member = getMember(ordersEntity.getMemberId());

        final MemberCoupon memberCoupon = getMemberCoupon(member, ordersEntity.getMemberCouponId());
        return new Order(
                orderEntityId,
                memberCoupon,
                member,
                makeOrderItems(orderItemDao.findAllByOrderId(orderEntityId))
        );
    }

    private Member getMember(final Long memberId) {
        return memberDao.findById(memberId)
                .orElseThrow(MemberNotFoundException::new)
                .toDomain();
    }

    private Coupon getCoupon(final Long couponId) {
        CouponEntity couponEntity = couponDao.findByCouponId(couponId).orElseThrow(CouponNotFoundException::new);
        DiscountPolicy discountPolicy = DiscountPolicyType.findDiscountPolicy(couponEntity.getPolicyType(),
                couponEntity.getDiscountPrice());

        return new Coupon(couponEntity.getId(), couponEntity.getName(), discountPolicy, couponEntity.getMinimumPrice());
    }
}
