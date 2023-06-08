package cart.controller.cartitem;

import cart.cartitem.application.CartItemService;
import cart.cartitem.presentation.dto.CartItemQuantityUpdateRequest;
import cart.cartitem.presentation.dto.CartItemRequest;
import cart.common.auth.Auth;
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
    public ResponseEntity<Void> addCartItems(@Auth Long memberId, @RequestBody CartItemRequest cartItemRequest) {
        Long cartItemId = cartItemService.add(memberId, cartItemRequest);

        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCartItemQuantity(@Auth Long memberId, @PathVariable Long id, @RequestBody CartItemQuantityUpdateRequest request) {
        cartItemService.updateQuantity(memberId, id, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItems(@Auth Long memberId, @PathVariable Long id) {
        cartItemService.remove(memberId, id);

        return ResponseEntity.noContent().build();
    }
}
