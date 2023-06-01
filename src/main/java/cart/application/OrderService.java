package cart.application;

import cart.dao.CartItemDao;
import cart.dao.CouponDao;
import cart.domain.CartItem;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.order.DeliveryFee;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.dto.request.OrderRequestDto;
import cart.dto.response.OrderResponseDto;
import cart.exception.CartItemNotFoundException;
import cart.exception.CouponNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final CartItemDao cartItemDao;
    private final CouponDao couponDao;

    public OrderService(final CartItemDao cartItemDao, final CouponDao couponDao) {
        this.cartItemDao = cartItemDao;
        this.couponDao = couponDao;
    }

    @Transactional
    public OrderResponseDto order(final Member member, final OrderRequestDto orderRequestDto) {
        final List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        checkCartItem(cartItems, orderRequestDto);

        final Order order = new Order(makeOrderProduct(cartItems, orderRequestDto));
        order.applyDeliveryFee(new DeliveryFee(3000));
        applyCoupon(member, orderRequestDto, order);

        return new OrderResponseDto(1L);
    }

    private void applyCoupon(final Member member, final OrderRequestDto orderRequestDto, final Order order) {
        if (Objects.isNull(orderRequestDto.getCouponId())) {
            return;
        }
        order.applyCoupon(findCoupon(member, orderRequestDto));
    }

    private Coupon findCoupon(final Member member, final OrderRequestDto orderRequestDto) {
        final List<Coupon> couponById = couponDao.findCouponById(member.getId());
        final Optional<Coupon> requestCoupon = couponById.stream().filter(coupon -> coupon.getId() == orderRequestDto.getCouponId()).findAny();
        if (requestCoupon.isEmpty()) {
            throw new CouponNotFoundException("해당 쿠폰을 유저가 가지고 있지 않습니다,");
        }
        return requestCoupon.get();
    }

    private void checkCartItem(final List<CartItem> cartItems, final OrderRequestDto orderRequestDto) {
        final List<Long> cartItemsId = cartItems.stream().map(CartItem::getId).collect(Collectors.toList());
        final boolean allItemExist = cartItemsId.containsAll(orderRequestDto.getCartItems());
        if (!allItemExist) {
            throw new CartItemNotFoundException("주문 요청에 존재하지 않는 카트 아이템이 포함되어 있습니다.");
        }
    }

    private List<OrderProduct> makeOrderProduct(final List<CartItem> cartItems, final OrderRequestDto orderRequestDto) {
        final List<Long> cartItemsId = orderRequestDto.getCartItems();
        return cartItems.stream()
                .filter(cartItem -> cartItemsId.contains(cartItem.getId()))
                .map(OrderProduct::of)
                .collect(Collectors.toList());
    }
}
