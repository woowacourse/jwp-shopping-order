package cart.controller.cart;

import cart.config.auth.Auth;
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
    public ResponseEntity<Void> addCartItems(@Auth final Member member,
                                             @RequestBody @Valid final CartItemRequest cartItemRequest) {
        Long cartItemId = cartService.add(member, cartItemRequest);
        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCartItemQuantity(@Auth final Member member,
                                                       @PathVariable("id") final Long cartItemId,
                                                       @RequestBody @Valid final CartItemQuantityUpdateRequest request) {
        cartService.updateQuantity(member, cartItemId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItems(@Auth final Member member,
                                                @PathVariable final Long id) {
        cartService.remove(member, id);
        return ResponseEntity.noContent().build();
    }
}
