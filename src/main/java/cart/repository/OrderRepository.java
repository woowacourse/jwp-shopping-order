package cart.repository;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.dao.OrderDao2;
import cart.dao.OrderItemDao;
import cart.dao.entity.CouponEntity;
import cart.dao.entity.MemberCouponEntity;
import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderItemEntity;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
        System.out.println(orderEntity);
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

        Map<Long, Coupon> couponById = getCouponByOrders(allOrderEntities);

        return allOrderEntities.stream()
                .map(orderEntity -> orderEntity.toOrder(member, orderItemByOrderId, couponById))
                .collect(Collectors.toList());
    }

    public Order findByIdAndMember(final Long id, final Member member) {
        OrderEntity orderEntity = orderDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));
        MemberCouponEntity memberCouponEntity = memberCouponDao.findById(orderEntity.getCouponId())
                .orElseThrow(() -> new IllegalArgumentException("멤버 쿠폰이 존재하지 않습니다."));
        CouponEntity couponEntity = couponDao.findById(memberCouponEntity.getId())
                .orElseThrow(() -> new IllegalArgumentException("쿠폰이 존재하지 않습니다."));

        Coupon coupon = memberCouponEntity.toCoupon(couponEntity);
        List<OrderItem> orderItems = orderItemDao.finByOrderId(orderEntity.getId())
                .stream()
                .map(OrderItemEntity::toOrderItem)
                .collect(Collectors.toList());
        return Order.of(orderItems, member, coupon);
    }

    private Map<Long, Coupon> getCouponByOrders(final List<OrderEntity> allOrderEntities) {
        List<Long> memberCouponIds = allOrderEntities.stream()
                .map(OrderEntity::getCouponId)
                .collect(Collectors.toList());

        List<MemberCouponEntity> memberCouponEntities = memberCouponDao.findByIds(memberCouponIds);
        Map<Long, MemberCouponEntity> memberCouponEntityById = memberCouponEntities.stream()
                .collect(Collectors.toMap(MemberCouponEntity::getId, memberCouponEntity -> memberCouponEntity));

        List<Long> couponIds = memberCouponEntities.stream()
                .map(MemberCouponEntity::getCouponId)
                .collect(Collectors.toList());

        List<CouponEntity> couponEntities = couponDao.findByIds(couponIds);
        Map<Long, CouponEntity> couponEntityById = couponEntities.stream()
                .collect(Collectors.toMap(CouponEntity::getId, couponEntity -> couponEntity));

        return memberCouponEntities.stream()
                .map(memberCouponEntity -> memberCouponEntity.toCoupon(couponEntityById))
                .collect(Collectors.toMap(coupon -> coupon.getCouponInfo().getId(), coupon -> coupon));
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
