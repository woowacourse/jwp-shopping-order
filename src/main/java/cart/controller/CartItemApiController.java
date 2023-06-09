package cart.controller;

import cart.auth.Authenticate;
import cart.auth.Credentials;
import cart.controller.dto.CartItemQuantityUpdateRequest;
import cart.controller.dto.CartItemRequest;
import cart.controller.dto.CartItemResponse;
import cart.service.CartItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/cart-items")
public class CartItemApiController {

    private final CartItemService cartItemService;

    public CartItemApiController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> showCartItems(@Authenticate Credentials credentials) {
        return ResponseEntity.ok(cartItemService.findByMember(credentials));
    }

    @PostMapping
    public ResponseEntity<Void> addCartItems(@Authenticate Credentials credentials, @RequestBody CartItemRequest cartItemRequest) {
        Long cartItemId = cartItemService.add(credentials, cartItemRequest);

        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCartItemQuantity(@Authenticate Credentials credentials, @PathVariable Long id, @RequestBody CartItemQuantityUpdateRequest request) {
        cartItemService.updateQuantity(credentials, id, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItems(@Authenticate Credentials credentials, @PathVariable Long id) {
        cartItemService.remove(credentials, id);

        return ResponseEntity.noContent().build();
    }
}
