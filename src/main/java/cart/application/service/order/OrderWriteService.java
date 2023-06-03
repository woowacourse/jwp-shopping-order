package cart.application.service.order;

import cart.application.repository.CartItemRepository;
import cart.application.repository.CouponRepository;
import cart.application.repository.PointRepository;
import cart.application.repository.order.OrderRepository;
import cart.application.repository.order.OrderedItemRepository;
import cart.domain.Member;
import cart.domain.PointHistory;
import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.CartItems;
import cart.domain.discountpolicy.CouponPolicy;
import cart.domain.discountpolicy.PointPolicy;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.exception.OverFullPointException;
import cart.ui.MemberAuth;
import cart.ui.order.dto.CreateOrderDto;
import cart.ui.order.dto.CreateOrderItemDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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

    public OrderWriteService(OrderRepository orderRepository, OrderedItemRepository orderedItemRepository, CartItemRepository cartItemRepository, CouponRepository couponRepository, PointRepository pointRepository, PointPolicy pointPolicy) {
        this.orderRepository = orderRepository;
        this.orderedItemRepository = orderedItemRepository;
        this.cartItemRepository = cartItemRepository;
        this.couponRepository = couponRepository;
        this.pointRepository = pointRepository;
        this.pointPolicy = pointPolicy;
    }

    public Long createOrder(MemberAuth memberAuth, CreateOrderDto createOrderDto) {
        Member member = new Member(memberAuth.getId(), memberAuth.getName(), memberAuth.getEmail(), memberAuth.getPassword());
        List<CreateOrderItemDto> createOrderItemDtos = createOrderDto.getCreateOrderItemDtos();
        CartItems cartItems = findCartItems(createOrderItemDtos);

        validateIsOwner(member, cartItems);

        int totalPrice = cartItems.calculateTotalPrice();
        List<Long> couponIds = createOrderDto.getCouponIds();
        List<CouponPolicy> couponPolicies = makeCoupon(couponIds);
        int paymentPrice = calculateApplyCoupon(totalPrice, couponPolicies);

        int usedPoint = createOrderDto.getPoint();
        validatePointAmount(paymentPrice, usedPoint);
        paymentPrice -= usedPoint;

        // 주문, 주문 상품 저장
        Long orderId = saveOrder(member, totalPrice, paymentPrice, usedPoint);
        saveOrderedItems(cartItems, orderId);

        // 멤버 쿠폰 status변경
        convertCouponStatusToUse(couponIds);

        // orderedCoupon 추가
        if (couponPolicies.size() > 0) {
            saveOrderCoupon(couponIds, orderId);
        }
        // pointhistory추가
        savePointHistory(member, paymentPrice, usedPoint, orderId);
        // cartitem삭제
        removeCartItems(cartItems);
        return orderId;
    }

    private Long saveOrder(Member member, int totalPrice, int paymentPrice, int usedPoint) {
        Order order = new Order(paymentPrice, totalPrice, usedPoint, member);
        Long orderId = orderRepository.createOrder(order);
        return orderId;
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

    private void validatePointAmount(int paymentPrice, int usedPoint) {
        if (usedPoint > paymentPrice) {
            throw new OverFullPointException("사용하려는 포인트가 결제 예상 금액보다 큽니다.");
        }
    }

    private int calculateApplyCoupon(int totalPrice, List<CouponPolicy> couponPolicies) {
        int totalDiscountAmount = couponPolicies.stream()
                .mapToInt(couponPolicy -> couponPolicy.applyDiscount(totalPrice))
                .sum();
        return totalPrice - totalDiscountAmount;
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

    private List<CouponPolicy> makeCoupon(List<Long> memberCouponIds) {
        if (memberCouponIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<CouponPolicy> couponPolicies = new ArrayList<>();

        for (Long memberCouponId : memberCouponIds) {
            Optional<CouponPolicy> percentCoupon = couponRepository.findPercentCouponById(memberCouponId);
            percentCoupon.ifPresent(couponPolicies::add);
        }

        for (Long memberCouponId : memberCouponIds) {
            Optional<CouponPolicy> amountCoupon = couponRepository.findAmountCouponById(memberCouponId);
            amountCoupon.ifPresent(couponPolicies::add);
        }

        validateIsValidCoupon(memberCouponIds, couponPolicies);
        return couponPolicies;
    }

    private static void validateIsValidCoupon(List<Long> memberCouponIds, List<CouponPolicy> couponPolicies) {
        if (memberCouponIds.size() != couponPolicies.size()) {
            throw new NoSuchElementException("존재하지않는 쿠폰입니다.");
        }
    }

}
