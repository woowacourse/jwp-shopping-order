package cart.repository;

import cart.dao.*;
import cart.dao.entity.*;
import cart.domain.*;
import cart.exception.CouponException;
import cart.exception.MemberException;
import cart.exception.OrderException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final CouponDao couponDao;
    private final MemberCouponDao memberCouponDao;
    private final MemberDao memberDao;

    public OrderRepository(final OrderDao orderDao, final OrderItemDao orderItemDao, final CouponDao couponDao, final MemberCouponDao memberCouponDao, final MemberDao memberDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.couponDao = couponDao;
        this.memberCouponDao = memberCouponDao;
        this.memberDao = memberDao;
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

    public Order findById(final Long id) {
        OrderEntity orderEntity = orderDao.findById(id)
                .orElseThrow(() -> new OrderException.NoExist("해당 주문이 존재하지 않습니다."));
        List<OrderItemEntity> orderItemEntities = orderItemDao.finByOrderId(orderEntity.getId());
        MemberEntity memberEntity = memberDao.findById(orderEntity.getMemberId())
                .orElseThrow(() -> new MemberException.NoExist("멤버가 존재하지 않습니다."));
        Member member = memberEntity.toMember();


        MemberCoupon memberCoupon = findMemberCoupon(orderEntity, member);

        List<OrderItem> orderItems = orderItemEntities.stream()
                .map(OrderItemEntity::toOrderItem)
                .collect(Collectors.toList());
        return Order.of(orderEntity.getId(), orderItems, member, memberCoupon);
    }

    private MemberCoupon findMemberCoupon(final OrderEntity orderEntity, final Member member) {
        Optional<MemberCouponEntity> maybeMemberCouponEntity = memberCouponDao.findById(orderEntity.getMemberCouponId());
        if (maybeMemberCouponEntity.isEmpty()) {
            return new EmptyMemberCoupon();
        }
        MemberCouponEntity memberCouponEntity = maybeMemberCouponEntity.get();
        CouponEntity couponEntity = couponDao.findById(memberCouponEntity.getCouponId())
                .orElseThrow(() -> new CouponException.NoExist("쿠폰이 존재하지 않습니다."));
        Coupon coupon = couponEntity.toCoupon();
        return memberCouponEntity.toMemberCoupon(coupon, member);
    }

    public void delete(final Order order) {
        Long id = order.getId();
        orderItemDao.deleteByOrderId(id);
        orderDao.deleteById(id);
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
