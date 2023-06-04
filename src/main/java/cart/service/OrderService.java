package cart.service;

import static cart.exception.ExceptionType.NOT_FOUND_CART_ITEM;
import static cart.exception.ExceptionType.NOT_FOUND_COUPON;
import static cart.exception.ExceptionType.NOT_FOUND_MEMBER;
import static cart.exception.ExceptionType.NOT_FOUND_ORDER;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Money;
import cart.domain.Order;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.MemberCoupon;
import cart.domain.coupon.repository.MemberCouponRepository;
import cart.domain.repository.CartItemRepository;
import cart.domain.repository.MemberRepository;
import cart.domain.repository.OrderRepository;
import cart.dto.AuthMember;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.exception.CartItemException;
import cart.exception.CouponException;
import cart.exception.ExceptionType;
import cart.exception.MemberException;
import cart.exception.OrderException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderService {

    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final CouponService couponService;
    private final MemberRepository memberRepository;

    public OrderService(CartItemRepository cartItemRepository, OrderRepository orderRepository,
                        MemberCouponRepository memberCouponRepository, CouponService couponService,
                        MemberRepository memberRepository) {
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.memberCouponRepository = memberCouponRepository;
        this.couponService = couponService;
        this.memberRepository = memberRepository;
    }

    public Long register(OrderRequest orderRequest, AuthMember authMember) {
        List<CartItem> cartItems = findAllCartItems(orderRequest.getCartItemIds());
        Member member = toMember(authMember);
        MemberCoupon memberCoupon = findMemberCoupon(orderRequest.getCouponId(), member);

        Order order = Order.of(member, cartItems, orderRequest.getDeliveryFee(), memberCoupon);

        Money totalPrice = order.calculateTotalPrice();
        if (totalPrice.isNotSameValue(orderRequest.getTotalOrderPrice())) {
            throw new OrderException(ExceptionType.INCORRECT_PRICE);
        }

        Order savedOrder = orderRepository.save(order);
        deleteOrdered(cartItems, memberCoupon);
        if (order.isUnusedCoupon()) {
            couponService.issueByOrderPrice(totalPrice, member);
        }
        return savedOrder.getId();
    }

    private List<CartItem> findAllCartItems(List<Long> cartItemIds) {
        if (cartItemIds.isEmpty()) {
            return Collections.emptyList();
        }
        return cartItemRepository.findByIds(cartItemIds);
    }

    private Member toMember(AuthMember authMember) {
        return memberRepository.findById(authMember.getId())
                .orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER));
    }

    private MemberCoupon findMemberCoupon(Long couponId, Member member) {
        if (couponId == -1L) {
            return new MemberCoupon(member, Coupon.NONE);
        }
        return memberCouponRepository.findById(couponId)
                .orElseThrow(() -> new CouponException(NOT_FOUND_COUPON));
    }

    private void deleteOrdered(List<CartItem> cartItems, MemberCoupon memberCoupon) {
        List<Long> cartItemIds = cartItems.stream()
                .map(CartItem::getId)
                .collect(Collectors.toList());
        if (!cartItemIds.isEmpty()) {
            cartItemRepository.deleteAllByIds(cartItemIds);
        }
        if (memberCoupon.isExists()) {
            memberCouponRepository.delete(memberCoupon);
        }
    }

    private CartItem findCartItem(Long cartItemId) {
        return cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemException(NOT_FOUND_CART_ITEM));
    }

    public OrderDetailResponse findById(Long id, AuthMember authMember) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderException(NOT_FOUND_ORDER));
        Member member = toMember(authMember);
        order.validateOwner(member);
        return OrderDetailResponse.from(order);
    }

    public List<OrderResponse> findAll(AuthMember authMember) {
        Member member = toMember(authMember);
        List<Order> orders = orderRepository.findAllByMember(member);

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }
}
