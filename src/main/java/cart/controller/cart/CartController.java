package cart.controller.cart;

import cart.cart.application.CartService;
import cart.common.auth.Auth;
import cart.controller.cart.dto.CartItemResponse;
import cart.controller.cart.dto.CouponResponse;
import cart.controller.cart.dto.DeliveryResponse;
import cart.controller.cart.dto.DiscountResponse;
import cart.coupon.application.CouponService;
import cart.member.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<CartItemResponse>> showCartItems(@Auth Member member) {
        final var cartItems = cartService.findCartItemsByMember(member);
        return ResponseEntity.ok(cartItems);
    }

    @GetMapping("/delivery-policy")
    public ResponseEntity<DeliveryResponse> showDeliveryPrice(@Auth Member member) {
        return ResponseEntity.ok(cartService.findDeliveryPrice(member));
    }

    @GetMapping("/coupons")
    public ResponseEntity<List<CouponResponse>> showCoupons(@Auth Member member) {
        return ResponseEntity.ok(couponService.findCouponsByMember(member.getId()));
    }

    @GetMapping(value = {"/cart-items/coupon/{couponIds}", "/cart-items/coupon"})
    public ResponseEntity<DiscountResponse> applyCoupon(@Auth Member member, @PathVariable(required = false) List<Long> couponIds) {
        if (couponIds == null) {
            couponIds = List.of();
        }
        return ResponseEntity.ok(cartService.discountWithCoupons(member, couponIds));
    }
}
