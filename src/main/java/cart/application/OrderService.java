package cart.application;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.dao.CartItemDao;
import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.CartItem;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.domain.Money;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.PriceCalculator;
import cart.dto.request.CartItemRequest;
import cart.dto.request.MemberCouponAddRequest;
import cart.dto.request.OrderRequest;
import cart.dto.response.CouponResponse;
import cart.dto.response.CouponsResponse;
import cart.dto.response.MemberCouponResponse;
import cart.dto.response.MemberCouponsResponse;
import cart.dto.response.OrderDetailResponse;
import cart.dto.response.OrderResponse;
import cart.dto.response.OrdersResponse;
import cart.exception.BadRequestException;
import cart.exception.ExceptionType;

@Transactional(readOnly = true)
@Service
public class OrderService {

    final CouponDao couponDao;
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final CartItemDao cartItemDao;
    private final MemberCouponDao memberCouponDao;

    public OrderService(OrderDao orderDao, OrderItemDao orderItemDao, CartItemDao cartItemDao,
        MemberCouponDao memberCouponDao, CouponDao couponDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.cartItemDao = cartItemDao;
        this.memberCouponDao = memberCouponDao;
        this.couponDao = couponDao;
    }

    public OrdersResponse getOrdersOf(Member member) {
        Long memberId = member.getId();
        List<Order> orders = orderDao.getOrdersByMemberId(memberId);
        return OrdersResponse.of(orders.stream().map(OrderResponse::of).collect(Collectors.toUnmodifiableList()));
    }

    public OrderDetailResponse getOrderOf(Member member, Long orderId) {
        orderDao.validate(member.getId(), orderId);

        Order orderWithoutOrderItems = orderDao.getOrderWithoutOrderItems(orderId);
        List<OrderItem> orderItems = orderItemDao.getAllItems(orderId);
        Order order = new Order(
            orderWithoutOrderItems.getId(),
            orderWithoutOrderItems.getMember(),
            orderItems,
            orderWithoutOrderItems.getMemberCoupon());
        return OrderDetailResponse.of(order);
    }

    @Transactional
    public Long add(Member member, OrderRequest orderRequest) {
        List<CartItemRequest> cartItemRequests = orderRequest.getProducts();

        if (cartItemRequests == null) {
            throw new BadRequestException(ExceptionType.CART_ITEM_EMPTY);
        }

        List<Long> cartItemIds = cartItemRequests
            .stream()
            .map(CartItemRequest::getCartItemId)
            .collect(Collectors.toUnmodifiableList());

        List<CartItem> cartItems = cartItemDao.findByIds(cartItemIds);

        if (cartItemRequests.size() != cartItems.size()) {
            throw new BadRequestException(ExceptionType.CART_ITEM_NO_EXIST);
        }
        List<OrderItem> orderItems = new ArrayList<>();
        for (int i = 0; i < cartItemRequests.size(); i++) {
            orderItems.add(
                new OrderItem(
                    null,
                    null,
                    cartItems.get(i).getId(),
                    cartItemRequests.get(i).getName(),
                    cartItemRequests.get(i).getPrice(),
                    cartItemRequests.get(i).getImageUrl(),
                    cartItemRequests.get(i).getQuantity()
                )
            );
        }

        Long memberCouponId = orderRequest.getCouponId();
        MemberCoupon memberCoupon = memberCouponDao.findByIdIfUsable(member.getId(), memberCouponId);

        Order order = new Order(member, orderItems, memberCoupon);

        Long orderId = orderDao.addOrder(order);
        orderItemDao.saveAllItems(orderId, order);
        memberCouponDao.updateUsabilityById(memberCouponId);
        cartItemDao.deleteByIds(cartItemIds);

        return orderId;
    }

    @Transactional
    public void deleteOrder(Member member, Long orderId) {
        orderDao.validate(member.getId(), orderId);
        orderItemDao.deleteAllOf(orderId);
        orderDao.delete(orderId);
    }

    public MemberCouponsResponse getMemberCoupons(Member member, List<Long> cartItemIds) {
        List<CartItem> cartItems = cartItemDao.findByIds(cartItemIds);
        Money price = PriceCalculator.calculate(cartItems);
        List<MemberCoupon> memberCoupons = memberCouponDao.findByMemberId(member.getId());

        return new MemberCouponsResponse(
            memberCoupons.stream()
                .map(memberCoupon -> MemberCouponResponse.of(memberCoupon, price))
                .collect(Collectors.toUnmodifiableList()));
    }

    public CouponsResponse getAllCoupons() {
        List<Coupon> coupons = couponDao.findAll();
        return new CouponsResponse(coupons.stream().map(CouponResponse::of).collect(Collectors.toUnmodifiableList()));
    }

    @Transactional
    public Long addMemberCoupon(Member member, Long couponId, MemberCouponAddRequest memberCouponAddRequest) {
        Coupon coupon = couponDao.findById(couponId);
        if (coupon == null) {
            throw new BadRequestException(ExceptionType.COUPON_NO_EXIST);
        }
        MemberCoupon memberCoupon = new MemberCoupon(null, member, coupon,
            Timestamp.valueOf(memberCouponAddRequest.getExpiredAt()));
        return memberCouponDao.save(memberCoupon);
    }
}
