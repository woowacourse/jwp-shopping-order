package cart.cart.application;

import cart.cart.Cart;
import cart.cart.domain.cartitem.application.CartItemRepository;
import cart.cart.presentation.dto.CartResponse;
import cart.cart.presentation.dto.DiscountResponse;
import cart.coupon.application.CouponService;
import cart.member.Member;
import cart.sale.SaleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final CouponService couponService;
    private final SaleService saleService;

    public CartService(CartItemRepository cartItemRepository, CouponService couponService, SaleService saleService) {
        this.cartItemRepository = cartItemRepository;
        this.couponService = couponService;
        this.saleService = saleService;
    }

    public CartResponse findCartByMemberId(Member member) {
        final Cart cart = findCart(member);

        return CartResponse.from(cart);
    }

    public DiscountResponse discountWithCoupons(Member member, List<Long> couponIds) {
        final var cart = findCart(member);
        for (Long couponId : couponIds) {
            couponService.applyCoupon(couponId, cart);
        }
        return DiscountResponse.from(cart);
    }

    private Cart findCart(Member member) {
        final var cartItems = cartItemRepository.findAllByMemberId(member.getId());
        final var coupons = member.getCouponIds()
                .stream().map(couponService::findById)
                .collect(Collectors.toList());
        final var cart = new Cart(cartItems, coupons);

        saleService.applySale(cart);
        return cart;
    }
}
