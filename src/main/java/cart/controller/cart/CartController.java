package cart.controller.cart;

import cart.config.auth.guard.basic.Auth;
import cart.domain.member.Member;
import cart.dto.cart.CartItemQuantityUpdateRequest;
import cart.dto.cart.CartItemRequest;
import cart.dto.cart.CartItemResponse;
import cart.service.cart.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/cart-items")
public class CartController {

    private final CartService cartService;

    public CartController(final CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> showCartItems(@Auth final Member member) {
        return ResponseEntity.ok(cartService.findByMember(member));
    }

    @PostMapping
    public ResponseEntity<Void> addCartItem(@Auth final Member member,
                                            @RequestBody @Valid final CartItemRequest cartItemRequest) {
        Long cartItemId = cartService.add(member, cartItemRequest);
        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @PatchMapping("/{cartItemId}")
    public ResponseEntity<Void> updateCartItemQuantity(@Auth final Member member,
                                                       @PathVariable final Long cartItemId,
                                                       @RequestBody @Valid final CartItemQuantityUpdateRequest request) {
        cartService.updateQuantity(member, cartItemId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(@Auth final Member member,
                                               @PathVariable final Long cartItemId) {
        cartService.remove(member, cartItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllCartItems(@Auth final Member member) {
        cartService.deleteAllCartItems(member);
        return ResponseEntity.noContent().build();
    }
}
