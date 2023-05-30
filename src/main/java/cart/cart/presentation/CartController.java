package cart.cart.presentation;

import cart.cart.application.CartService;
import cart.cart.presentation.dto.CartResponse;
import cart.cart.presentation.dto.CouponDiscountRequest;
import cart.cart.presentation.dto.DiscountResponse;
import cart.common.auth.Auth;
import cart.member.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartResponse> findCart(@Auth Member member) {
        final var cart = cartService.findCartByMemberId(member);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/coupons")
    public ResponseEntity<DiscountResponse> selectCoupons(@Auth Member member, @RequestBody CouponDiscountRequest couponDiscountRequest) {
        final var discountResponse = cartService.discountWithCoupons(member, couponDiscountRequest.getCouponIds());
        return ResponseEntity.ok(discountResponse);
    }

}
