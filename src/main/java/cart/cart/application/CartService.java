package cart.cart.application;

import cart.cart.domain.cartitem.application.CartItemRepository;
import cart.cart.presentation.dto.CartResponse;
import cart.coupon.application.CouponRepository;
import cart.discountpolicy.application.DiscountPolicyRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final DiscountPolicyRepository discountPolicyRepository;
    private final CouponRepository couponRepository;

    public CartService(CartItemRepository cartItemRepository, DiscountPolicyRepository discountPolicyRepository, CouponRepository couponRepository) {
        this.cartItemRepository = cartItemRepository;
        this.discountPolicyRepository = discountPolicyRepository;
        this.couponRepository = couponRepository;
    }

    public CartResponse findCartByMemberId(Long memberId) {
        final var cartItems = cartItemRepository.findByMemberId(memberId);
        final var policies = discountPolicyRepository.findAllNonSelectivePolicies();
        cartItems.forEach(cartItem -> cartItem.discountPrice(policies));
        final var deliveryPrice = getDeliveryPrice();
        final var coupons = couponRepository.findAllByMemberId(memberId);

        return CartResponse.from(cartItems, deliveryPrice, coupons);
    }

    private int getDeliveryPrice() {
        return 3_000;
    }
}
