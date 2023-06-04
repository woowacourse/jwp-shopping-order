package cart.repository;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.dao.OrderDao2;
import cart.dao.OrderItemDao;
import cart.dao.entity.CouponEntity;
import cart.dao.entity.MemberCouponEntity;
import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderItemEntity;
import cart.domain.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {
    private final OrderDao2 orderDao;
    private final OrderItemDao orderItemDao;
    private final CouponDao couponDao;
    private final MemberCouponDao memberCouponDao;

    public OrderRepository(final OrderDao2 orderDao, final OrderItemDao orderItemDao, final CouponDao couponDao, final MemberCouponDao memberCouponDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.couponDao = couponDao;
        this.memberCouponDao = memberCouponDao;
    }

    public Long save(final Order order) {
        OrderEntity orderEntity = OrderEntity.from(order);
        Long savedOrderId = orderDao.save(orderEntity);

        List<OrderItemEntity> orderItemEntities = order.getOrderItems()
                .stream()
                .map(orderItem -> OrderItemEntity.of(orderItem, savedOrderId))
                .collect(Collectors.toList());

        orderItemDao.saveAll(orderItemEntities);
        return savedOrderId;
    }

    // TODO: 5/31/23 주문 가져오는데 진짜 큰일을 함 / 지연로딩이 필요한가?
    public List<Order> findAllByMember(final Member member) {
        List<OrderEntity> allOrderEntities = orderDao.findByMemberId(member.getId());

        List<Long> orderIds = allOrderEntities.stream()
                .map(OrderEntity::getId)
                .collect(Collectors.toList());

        Map<Long, List<OrderItem>> orderItemByOrderId = getOrderItemByOrderId(allOrderEntities, orderIds);

        Map<Long, MemberCoupon> couponById = findMemberCouponByOrderEntitiesAndMember(allOrderEntities, member);

        return allOrderEntities.stream()
                .map(orderEntity -> orderEntity.toOrder(member, orderItemByOrderId, couponById))
                .collect(Collectors.toList());
    }

    public Order findByIdAndMember(final Long id, final Member member) {
        OrderEntity orderEntity = orderDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));
        MemberCouponEntity memberCouponEntity = memberCouponDao.findById(orderEntity.getMemberCouponId())
                .orElseThrow(() -> new IllegalArgumentException("멤버 쿠폰이 존재하지 않습니다."));
        CouponEntity couponEntity = couponDao.findById(memberCouponEntity.getId())
                .orElseThrow(() -> new IllegalArgumentException("쿠폰이 존재하지 않습니다."));

        MemberCoupon memberCoupon = memberCouponEntity.toMemberCoupon(couponEntity.toCoupon(), member);
        List<OrderItem> orderItems = orderItemDao.finByOrderId(orderEntity.getId())
                .stream()
                .map(OrderItemEntity::toOrderItem)
                .collect(Collectors.toList());
        return Order.of(orderEntity.getId(), orderItems, member, memberCoupon);
    }

    private Map<Long, MemberCoupon> findMemberCouponByOrderEntitiesAndMember(final List<OrderEntity> orderEntities, final Member member) {
        List<Long> memberCouponIds = orderEntities.stream()
                .map(OrderEntity::getMemberCouponId)
                .collect(Collectors.toList());

        List<MemberCouponEntity> memberCouponEntities = memberCouponDao.findByIds(memberCouponIds);

        List<Long> couponIds = memberCouponEntities.stream()
                .map(MemberCouponEntity::getCouponId)
                .collect(Collectors.toList());
        List<CouponEntity> couponEntities = couponDao.findByIds(couponIds);
        Map<Long, Coupon> coupons = couponEntities.stream()
                .collect(Collectors.toMap(CouponEntity::getId, CouponEntity::toCoupon));

        return memberCouponEntities.stream()
                .map(memberCouponEntity -> memberCouponEntity.toMemberCoupon(coupons.get(memberCouponEntity.getCouponId()), member))
                .collect(Collectors.toMap(MemberCoupon::getId, coupon -> coupon));
    }

    private Map<Long, List<OrderItem>> getOrderItemByOrderId(final List<OrderEntity> allOrderEntities, final List<Long> orderIds) {
        List<OrderItemEntity> orderItemEntities = orderItemDao.findByOrderIds(orderIds);
        Map<Long, List<OrderItem>> orderItemByOrderId = allOrderEntities.stream()
                .collect(Collectors.toMap(OrderEntity::getId, orderEntity -> new ArrayList<>()));
        for (OrderItemEntity orderItemEntity : orderItemEntities) {
            orderItemByOrderId.get(orderItemEntity.getOrderId())
                    .add(orderItemEntity.toOrderItem());
        }
        return orderItemByOrderId;
    }
}
