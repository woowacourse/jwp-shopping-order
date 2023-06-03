package cart.application.service.order;

import cart.application.repository.cartItem.CartItemRepository;
import cart.application.repository.coupon.CouponRepository;
import cart.application.repository.point.PointRepository;
import cart.application.service.order.dto.CreateOrderDiscountDto;
import cart.application.service.order.dto.CreateOrderDto;
import cart.domain.cartitem.CartItems;
import cart.domain.coupon.Coupons;
import cart.domain.discountpolicy.StoreDiscountPolicy;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderItems;
import cart.domain.point.Point;
import cart.domain.point.PointHistories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderMakeService {
    private final CartItemRepository cartItemRepository;
    private final CouponRepository couponRepository;
    private final StoreDiscountPolicy storeDiscountPolicy;
    private final PointRepository pointRepository;

    public OrderMakeService(
            final CartItemRepository cartItemRepository,
            final CouponRepository couponRepository,
            final StoreDiscountPolicy storeDiscountPolicy,
            final PointRepository pointRepository
    ) {
        this.cartItemRepository = cartItemRepository;
        this.couponRepository = couponRepository;
        this.storeDiscountPolicy = storeDiscountPolicy;
        this.pointRepository = pointRepository;
    }

    public Order makeOrder(final Member member, final CreateOrderDto createOrderDto) {
        final CreateOrderDiscountDto createOrderDiscountDto = createOrderDto.getCreateOrderDiscountDto();
        final CartItems cartItems = creatCartItems(member, createOrderDto);
        final Point point = createPoint(member, createOrderDiscountDto.getPoint());
        final Coupons coupons = creatCoupons(member, createOrderDiscountDto);

        final int paymentPrice = storeDiscountPolicy.calculateStoreDiscount(cartItems.calculateTotalPrice(), coupons, point);
        return new Order(member, makeOrderItems(cartItems), coupons, paymentPrice, point);
    }

    public int getPaymentPrice(final Member member, final CreateOrderDto createOrderDto) {
        final Order order = makeOrder(member, createOrderDto);

        return order.getPaymentPrice();
    }

    private Coupons creatCoupons(final Member member, final CreateOrderDiscountDto createOrderDiscountDto) {
        final List<Long> couponIds = createOrderDiscountDto.getCouponIds();
        if (couponIds.isEmpty()) {
            return new Coupons(Collections.emptyList());
        }
        final Coupons coupons = couponRepository.findMemberCouponByCouponIds(member.getId(), createOrderDiscountDto.getCouponIds());
        coupons.validateCouponOwnership(createOrderDiscountDto.getCouponIds());
        return coupons;
    }

    private CartItems creatCartItems(final Member member, final CreateOrderDto createOrderDto) {
        final CartItems cartItems = new CartItems(createOrderDto.getCreateOrderItemDtos().stream()
                .map(createOrderItemDto -> cartItemRepository.findById(createOrderItemDto.getCartItemId())
                        .orElseThrow(() -> new IllegalArgumentException("장바구니에 일치하는 상품이 없습니다."))
                ).collect(Collectors.toUnmodifiableList()));
        cartItems.validate(createOrderDto.getCreateOrderItemDtos(), member.getId());
        return cartItems;
    }

    private Point createPoint(final Member member, final Integer requestPoint) {
        final Point point = new Point(requestPoint);
        final PointHistories pointHistories = pointRepository.findPointByMemberId(member.getId());
        pointHistories.validatePointOwnership(point);

        return point;
    }

    private OrderItems makeOrderItems(final CartItems cartItems) {
        return new OrderItems(cartItems.getCartItems().stream()
                .map(cartItem -> OrderItem.of(cartItem.getQuantity(), cartItem.getProduct()))
                .collect(Collectors.toUnmodifiableList()));
    }
}
