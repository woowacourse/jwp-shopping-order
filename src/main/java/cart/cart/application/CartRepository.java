package cart.cart.application;

import cart.cart.Cart;
import cart.cart.domain.cartitem.application.CartItemRepository;
import cart.coupon.application.CouponService;
import cart.member.Member;
import org.springframework.stereotype.Repository;

import java.util.stream.Collectors;

@Repository
public class CartRepository {
    private final CartItemRepository cartItemRepository;
    private final CouponService couponService;

    public CartRepository(CartItemRepository cartItemRepository, CouponService couponService) {
        this.cartItemRepository = cartItemRepository;
        this.couponService = couponService;
    }

    public Cart findCart(Member member) {
        final var cartItems = cartItemRepository.findAllByMemberId(member.getId());
        final var coupons = member.getCouponIds()
                .stream().map(couponService::findById)
                .collect(Collectors.toList());
        return new Cart(cartItems, coupons);
    }
}
