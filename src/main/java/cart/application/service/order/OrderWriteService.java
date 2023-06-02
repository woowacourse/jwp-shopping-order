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
import cart.ui.order.CreateOrderDto;
import cart.ui.order.CreateOrderItemDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
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

    public OrderWriteService(final OrderRepository orderRepository, final OrderedItemRepository orderedItemRepository, final CartItemRepository cartItemRepository, final CouponRepository couponRepository, final PointRepository pointRepository, final PointPolicy pointPolicy) {
        this.orderRepository = orderRepository;
        this.orderedItemRepository = orderedItemRepository;
        this.cartItemRepository = cartItemRepository;
        this.couponRepository = couponRepository;
        this.pointRepository = pointRepository;
        this.pointPolicy = pointPolicy;
    }

    // TODO : 잔여 포인트와 결제에 사용할 포인트 비교
    public Long createOrder(final MemberAuth memberAuth, final CreateOrderDto createOrderDto) {
        final Member member = new Member(memberAuth.getId(), memberAuth.getName(), memberAuth.getEmail(), memberAuth.getPassword());
        List<CreateOrderItemDto> createOrderItemDtos = createOrderDto.getCreateOrderItemDtos();

        CartItems cartItems = findCartItems(createOrderItemDtos);

        int totalPrice = cartItems.calculateTotalPrice();
        if (cartItems.isNotOwnedMember(member)) {
            throw new IllegalArgumentException("장바구니 소유자가 올바르지 않습니다.");
        }

        List<Long> couponIds = createOrderDto.getCreateOrderDiscountDto().getCouponIds();
        List<CouponPolicy> couponPolicies = makeCoupon(couponIds);

        int totalDiscountAmount = couponPolicies.stream()
                .mapToInt(couponPolicy -> couponPolicy.applyDiscount(totalPrice))
                .sum();
        int paymentPrice = totalPrice - totalDiscountAmount;

        int usedPoint = createOrderDto.getCreateOrderDiscountDto().getPoint();
        if (usedPoint > paymentPrice) {
            throw new OverFullPointException("사용하려는 포인트가 결제 예상 금액보다 큽니다.");
        }

        paymentPrice -= usedPoint;

        int finalPaymentPrice = paymentPrice;


        //오더 만듬
        final Order order = new Order(finalPaymentPrice, totalPrice, usedPoint, member, cartItems.getCartItems().stream()
                .map(cartItem1 -> OrderItem.of(cartItem1.getQuantity(), cartItem1.getProduct()))
                .collect(Collectors.toUnmodifiableList()));

//        오더 저장
        Long orderId = orderRepository.createOrder(order);
//        오더 아이템 저장
        orderedItemRepository.createOrderItems(orderId, order.getOrderItems());


        // 멤버 쿠폰 status변경
        for (Long memberCouponId : couponIds) {
            couponRepository.convertToUseMemberCoupon(memberCouponId);
        }

        // orderedCoupon 추가, orderedCouponDto사용?? or 객체 생성??
        if (couponPolicies.size() > 0) {
            for (Long memberCouponId : couponIds) {
                couponRepository.createOrderedCoupon(orderId, memberCouponId);
            }
        }
        // pointhistory추가
        int earnedPoint = pointPolicy.calculateEarnedPoint(paymentPrice);
        pointRepository.createPointHistory(member.getId(), new PointHistory(orderId, earnedPoint, usedPoint));
        // cartitem삭제
        for (CartItem cartItem : cartItems.getCartItems()) {
            cartItemRepository.deleteById(cartItem.getId());
        }
        return orderId;
    }

    private CartItems findCartItems(final List<CreateOrderItemDto> createOrderItemDtos) {
        Map<Long, CartItem> cartItemsPerId = makeCartItemsPerId(createOrderItemDtos);
        List<CartItem> cartItems = cartItemsPerId.values().stream()
                .collect(Collectors.toUnmodifiableList());
        return new CartItems(cartItems);
    }

    private Map<Long, CartItem> makeCartItemsPerId(final List<CreateOrderItemDto> createOrderItemDtos) {
        return createOrderItemDtos.stream()
                .map(CreateOrderItemDto::getCartItemId)
                .map(cartItemId -> cartItemRepository.findById(cartItemId).orElseThrow(() -> new NoSuchElementException("유효하지 않은 장바구니입니다.")))
                .collect(Collectors.toUnmodifiableMap(CartItem::getId, Function.identity()));
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
            Optional<CouponPolicy> percentCoupon = couponRepository.findAmountCouponById(memberCouponId);
            percentCoupon.ifPresent(couponPolicies::add);
        }
        return couponPolicies;
    }

}
