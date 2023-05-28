package cart.cart.presentation;

import cart.cart.application.CartService;
import cart.cart.presentation.dto.CartResponse;
import cart.common.auth.Auth;
import cart.member.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartResponse> findCart(@Auth Member member) {
        final var cart = cartService.findCartByMemberId(member.getId());
        return ResponseEntity.ok(cart);
    }
}
