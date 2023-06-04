package cart.repository;

import cart.dao.*;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.DiscountType;
import cart.domain.repository.OrderRepository;
import cart.entity.CouponEntity;
import cart.entity.OrderEntity;
import cart.entity.OrderProductEntity;
import cart.exception.OrderException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderDao orderDao;
    private final MemberDao memberDao;
    private final OrderProductDao orderProductDao;
    private final OrderCouponDao orderCouponDao;
    private final CartItemDao cartItemDao;
    private final MemberCouponDao memberCouponDao;

    public OrderRepositoryImpl(OrderDao orderDao, MemberDao memberDao, OrderProductDao orderProductDao,
                               OrderCouponDao orderCouponDao, CartItemDao cartItemDao, MemberCouponDao memberCouponDao) {
        this.orderDao = orderDao;
        this.memberDao = memberDao;
        this.orderProductDao = orderProductDao;
        this.orderCouponDao = orderCouponDao;
        this.cartItemDao = cartItemDao;
        this.memberCouponDao = memberCouponDao;
    }

    @Override
    public Long save(Order order) {
        Long savedOrderId = orderDao.save(toEntity(order));

        List<OrderProductEntity> orderProducts = order.getCartProducts().stream()
                .map(it -> OrderProductEntity.of(it, savedOrderId)).collect(Collectors.toList());
        List<Long> cartItemIds = order.getCartProducts().stream()
                .map(CartItem::getId)
                .collect(Collectors.toList());

        cartItemDao.deleteByIdsAndMemberId(order.getMember().getId(), cartItemIds);
        orderProductDao.save(savedOrderId, orderProducts);
        memberCouponDao.updateUnUsedCouponAvailabilityById(order.getCoupon().getId());
        if (!DiscountType.EMPTY_DISCOUNT.getTypeName().equals(order.getCoupon().getCouponTypes().getCouponTypeName())) {
            orderCouponDao.save(savedOrderId, order.getCoupon().getId());
        }
        return savedOrderId;
    }

    @Override
    public List<Order> findAllByMemberId(Member member) {
        return orderDao.findAllByMemberId(member.getId()).stream()
                .map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public Order findByIdAndMemberId(Member member, Long orderId) {
        return toDomain(orderDao.findByIdAndMemberId(member.getId(), orderId).orElseThrow(() -> new OrderException("주문 상세정보를 찾을 수 없습니다.")));
    }

    @Override
    public void deleteById(Member member, Long orderId) {
        if (orderCouponDao.checkByOrderId(orderId)) {
            Long memberCouponId = orderCouponDao.findIdByOrderId(orderId);
            orderCouponDao.deleteByOrderId(orderId);
            memberCouponDao.updateUnUsedCouponAvailabilityById(memberCouponId);
        }

        orderDao.deleteById(orderId);
    }

    @Override
    public void confirmById(Long orderId, Member member) {
        OrderEntity orderEntity = orderDao.findByIdAndMemberId(member.getId(), orderId).orElseThrow(() -> new OrderException("잘못된 주문입니다."));
        orderDao.confirmByOrderIdAndMemberId(orderEntity.getId(), member.getId());
    }

    @Override
    public boolean checkConfirmStateById(Long orderId) {
        return orderDao.checkConfirmStateById(orderId);
    }

    private OrderEntity toEntity(Order order) {
        return new OrderEntity(
                order.getMember().getId(),
                order.calculatePrice(),
                order.calculateDiscountPrice());
    }

    private Order toDomain(OrderEntity orderEntity) {
        Member member = memberDao.findById(orderEntity.getMemberId());
        List<CartItem> products = orderProductDao.findOrderProductByOrderId(orderEntity.getId()).stream()
                .map(it -> new CartItem(it.getId(), it.getQuantity(),
                        new Product(it.getId(), it.getName(), it.getPrice(), it.getImage_url()), member))
                .collect(Collectors.toList());
        CouponEntity couponEntity = orderCouponDao.findByOrderId(orderEntity.getId()).orElse(CouponEntity.EMPTY);
        Coupon coupon = new Coupon(couponEntity.getId(), couponEntity.getName(), DiscountType.from(couponEntity.getDiscountType()),
                couponEntity.getMinimumPrice(), couponEntity.getDiscountPrice(), couponEntity.getDiscountRate());
        return new Order(orderEntity.getId(), member, products, orderEntity.getConfirmState(), coupon);
    }
}
