package cart.repository;

import cart.domain.Member;
import cart.domain.Money;
import cart.domain.coupon.MemberCoupon;
import cart.domain.coupon.MemberCoupons;
import cart.domain.discountpolicy.DiscountPolicyProvider;
import cart.domain.order.Order;
import cart.domain.product.OrderItem;
import cart.repository.dao.OrderCouponDao;
import cart.repository.dao.OrdersDao;
import cart.repository.entity.OrderCouponEntity;
import cart.repository.entity.OrdersEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {

    private final OrderItemRepository orderItemRepository;
    private final OrderCouponDao orderCouponDao;
    private final OrdersDao ordersDao;
    private final MemberCouponRepository memberCouponRepository;
    private final MemberRepository memberRepository;
    private final DiscountPolicyProvider discountPolicyProvider;

    public OrderRepository(OrderItemRepository orderItemRepository,
                           OrderCouponDao orderCouponDao,
                           OrdersDao ordersDao,
                           MemberCouponRepository memberCouponRepository,
                           MemberRepository memberRepository,
                           DiscountPolicyProvider discountPolicyProvider
    ) {
        this.orderItemRepository = orderItemRepository;
        this.orderCouponDao = orderCouponDao;
        this.ordersDao = ordersDao;
        this.memberCouponRepository = memberCouponRepository;
        this.memberRepository = memberRepository;
        this.discountPolicyProvider = discountPolicyProvider;
    }

    public Long insert(Order order) {
        Long orderId = ordersDao.insert(OrdersEntity.of(order));

        orderItemRepository.insertAll(order.getOrderItems(), orderId);

        List<OrderCouponEntity> orderCouponEntities = order.getAppliedCoupons().stream()
                .map(memberCoupon -> OrderCouponEntity.of(memberCoupon, orderId))
                .collect(Collectors.toList());
        orderCouponDao.insertAll(orderCouponEntities);

        return orderId;
    }

    public Order findById(Long orderId) {
        OrdersEntity ordersEntity = ordersDao.findById(orderId);
        return getOrder(ordersEntity);
    }

    private Order getOrder(OrdersEntity ordersEntity) {
        List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(ordersEntity.getId());
        List<Long> memberCouponIds = orderCouponDao.findAllByOrderId(ordersEntity.getId()).stream()
                .map(OrderCouponEntity::getMemberCouponId)
                .collect(Collectors.toList());
        List<MemberCoupon> appliedCoupons = memberCouponRepository.findAllById(memberCouponIds);
        Member orderOwner = memberRepository.getMemberById(ordersEntity.getMemberId());

        return makeOrderBy(ordersEntity, orderItems, appliedCoupons, orderOwner);
    }

    private Order makeOrderBy(
            OrdersEntity ordersEntity,
            List<OrderItem> orderItems,
            List<MemberCoupon> appliedCoupons,
            Member orderOwner
    ) {
        return new Order(
                ordersEntity.getId(),
                new Money(ordersEntity.getOriginalPrice()),
                new Money(ordersEntity.getActualPrice()),
                MemberCoupons.of(appliedCoupons, discountPolicyProvider),
                orderItems,
                new Money(ordersEntity.getDeliveryFee()),
                orderOwner
        );
    }

    public List<Order> findAllByMember(Member member) {
        List<OrdersEntity> ordersEntities = ordersDao.findAllByMemberId(member.getId());
        return ordersEntities.stream()
                .map(this::getOrder)
                .collect(Collectors.toList());
    }
}
