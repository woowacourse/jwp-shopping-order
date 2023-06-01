package cart.cart.domain.cartitem.presentation;

import cart.cart.domain.cartitem.application.CartItemService;
import cart.cart.domain.cartitem.presentation.dto.CartItemQuantityUpdateRequest;
import cart.cart.domain.cartitem.presentation.dto.CartItemRequest;
import cart.common.auth.Auth;
import cart.member.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/cart-items")
public class CartItemApiController {

    private final CartItemService cartItemService;

    public CartItemApiController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping
    public ResponseEntity<Void> addCartItems(@Auth Member member, @RequestBody CartItemRequest cartItemRequest) {
        Long cartItemId = cartItemService.add(member, cartItemRequest);

        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCartItemQuantity(@Auth Member member, @PathVariable Long id, @RequestBody CartItemQuantityUpdateRequest request) {
        cartItemService.updateQuantity(member, id, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItems(@Auth Member member, @PathVariable Long id) {
        cartItemService.remove(member, id);

        return ResponseEntity.noContent().build();
    }
}
