package cart.application.service.order;

import cart.application.repository.CartItemRepository;
import cart.application.repository.CouponRepository;
import cart.application.repository.PointRepository;
import cart.application.repository.order.OrderRepository;
import cart.application.repository.order.OrderedItemRepository;
import cart.domain.PointHistory;
import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.CartItems;
import cart.domain.coupon.Coupon;
import cart.domain.discountpolicy.CouponType;
import cart.domain.discountpolicy.DiscountPolicy;
import cart.domain.discountpolicy.PointPolicy;
import cart.domain.discountpolicy.ZlzonStoreDiscountPolicy;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.ui.MemberAuth;
import cart.ui.order.dto.CreateOrderDto;
import cart.ui.order.dto.CreateOrderItemDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderWriteService {

    private final OrderRepository orderRepository;
    private final OrderedItemRepository orderedItemRepository;
    private final CartItemRepository cartItemRepository;
    private final CouponRepository couponRepository;
    private final PointRepository pointRepository;
    private final PointPolicy pointPolicy;
    private final DiscountPolicy discountPolicy;

    public OrderWriteService(OrderRepository orderRepository, OrderedItemRepository orderedItemRepository, CartItemRepository cartItemRepository, CouponRepository couponRepository, PointRepository pointRepository, PointPolicy pointPolicy, ZlzonStoreDiscountPolicy discountPolicy) {
        this.orderRepository = orderRepository;
        this.orderedItemRepository = orderedItemRepository;
        this.cartItemRepository = cartItemRepository;
        this.couponRepository = couponRepository;
        this.pointRepository = pointRepository;
        this.pointPolicy = pointPolicy;
        this.discountPolicy = discountPolicy;
    }

    public Long createOrder(MemberAuth memberAuth, CreateOrderDto createOrderDto) {
        Member member = new Member(memberAuth.getId(), memberAuth.getName(), memberAuth.getEmail(), memberAuth.getPassword());
        CartItems cartItems = makeCartItems(createOrderDto);
        validateIsOwner(member, cartItems);

        // 쿠폰 생성
        List<Long> couponIds = createOrderDto.getCouponIds();
        List<CouponType> coupons = makeCoupon(couponIds);

        // 주문, 주문 아이템 저장
        Order order = makeOrder(cartItems, createOrderDto, member, coupons);
        Long orderId = orderRepository.createOrder(order);
        saveOrderedItems(cartItems, orderId);
        useCoupon(couponIds, coupons, orderId);
        // pointhistory추가
        savePointHistory(member, order.getPaymentPrice(), order.getPoint(), orderId);
        // cartitem삭제
        removeCartItems(cartItems);
        return orderId;
    }

    private Order makeOrder(CartItems cartItems, CreateOrderDto createOrderDto, Member member, List<CouponType> coupons) {
        int totalPrice = cartItems.calculateTotalPrice();
        int usedPoint = createOrderDto.getPoint();
        int paymentPrice = discountPolicy.discount(totalPrice, coupons, usedPoint);
        Order order = new Order(paymentPrice, totalPrice, usedPoint, member);

        return order;
    }

    private CartItems makeCartItems(CreateOrderDto createOrderDto) {
        List<CreateOrderItemDto> createOrderItemDtos = createOrderDto.getCreateOrderItemDtos();
        if (createOrderItemDtos.isEmpty()) {
            throw new IllegalArgumentException("주문하려는 상품이 0개입니다.");
        }
        CartItems cartItems = findCartItems(createOrderItemDtos);
        return cartItems;
    }

    private void useCoupon(List<Long> couponIds, List<CouponType> coupons, Long orderId) {
        convertCouponStatusToUse(couponIds);

        if (coupons.size() > 0) {
            saveOrderCoupon(couponIds, orderId);
        }
    }

    private void saveOrderedItems(CartItems cartItems, Long orderId) {
        List<OrderItem> orderItems = cartItems.getCartItems().stream()
                .map(cartItem -> OrderItem.of(orderId, cartItem.getQuantity(), cartItem.getProduct()))
                .collect(Collectors.toUnmodifiableList());
        orderedItemRepository.createOrderItems(orderItems);
    }

    private void removeCartItems(CartItems cartItems) {
        for (CartItem cartItem : cartItems.getCartItems()) {
            cartItemRepository.deleteById(cartItem.getId());
        }
    }

    private void savePointHistory(Member member, int paymentPrice, int usedPoint, Long orderId) {
        int earnedPoint = pointPolicy.calculateEarnedPoint(paymentPrice);
        pointRepository.createPointHistory(member.getId(), new PointHistory(orderId, earnedPoint, usedPoint));
    }

    private void saveOrderCoupon(List<Long> couponIds, Long orderId) {
        for (Long memberCouponId : couponIds) {
            couponRepository.createOrderedCoupon(orderId, memberCouponId);
        }
    }

    private void convertCouponStatusToUse(List<Long> couponIds) {
        for (Long memberCouponId : couponIds) {
            couponRepository.convertToUseMemberCoupon(memberCouponId);
        }
    }

    private void validateIsOwner(Member member, CartItems cartItems) {
        if (cartItems.isNotOwnedMember(member)) {
            throw new IllegalArgumentException("장바구니 소유자가 올바르지 않습니다.");
        }
    }

    private CartItems findCartItems(final List<CreateOrderItemDto> createOrderItemDtos) {

        List<CartItem> cartItems = createOrderItemDtos.stream()
                .map(CreateOrderItemDto::getCartItemId)
                .map(cartItemId -> cartItemRepository.findById(cartItemId).orElseThrow(() -> new NoSuchElementException("유효하지 않은 장바구니입니다.")))
                .collect(Collectors.toUnmodifiableList());
        return new CartItems(cartItems);
    }

    private List<CouponType> makeCoupon(List<Long> memberCouponIds) {
        if (memberCouponIds.isEmpty()) {
            return Collections.emptyList();
        }
        return memberCouponIds.stream()
                .map(couponRepository::findUsableCouponByMemberCouponId)
                .map(coupon -> coupon.orElseThrow(() -> new NoSuchElementException("유효하지 않은 쿠폰입니다.")))
                .map(Coupon::makeFitCouponType)
                .collect(Collectors.toUnmodifiableList());
    }

}
