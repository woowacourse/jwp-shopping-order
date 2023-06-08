package cart.controller.cart;

import cart.cart.application.CartService;
import cart.common.auth.Auth;
import cart.controller.cart.dto.CartItemResponse;
import cart.controller.cart.dto.CouponResponse;
import cart.controller.cart.dto.DeliveryResponse;
import cart.controller.cart.dto.DiscountResponse;
import cart.coupon.application.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CartController {
    private final CartService cartService;
    private final CouponService couponService;

    public CartController(CartService cartService, CouponService couponService) {
        this.cartService = cartService;
        this.couponService = couponService;
    }

    @GetMapping("/cart-items")
    public ResponseEntity<List<CartItemResponse>> showCartItems(@Auth Long memberId) {
        final var cartItems = cartService.findCartItemsByMember(memberId);
        return ResponseEntity.ok(cartItems);
    }

    @GetMapping("/delivery-policy")
    public ResponseEntity<DeliveryResponse> showDeliveryPrice(@Auth Long memberId) {
        return ResponseEntity.ok(cartService.findDeliveryPrice(memberId));
    }

    @GetMapping("/coupons")
    public ResponseEntity<List<CouponResponse>> showCoupons(@Auth Long memberId) {
        return ResponseEntity.ok(couponService.findCouponsByMember(memberId));
    }

    @GetMapping(value = {"/cart-items/coupon/{couponIds}", "/cart-items/coupon"})
    public ResponseEntity<DiscountResponse> applyCoupon(@Auth Long memberId, @PathVariable(required = false) List<Long> couponIds) {
        if (couponIds == null) {
            couponIds = List.of();
        }
        return ResponseEntity.ok(cartService.discountWithCoupons(memberId, couponIds));
    }
}
