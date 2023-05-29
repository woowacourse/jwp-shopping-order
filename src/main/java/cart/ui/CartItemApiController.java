package cart.ui;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import cart.application.CartItemService;
import cart.config.AuthPrincipal;
import cart.dto.AuthMember;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart-items")
public class CartItemApiController {

    private final CartItemService cartItemService;

    public CartItemApiController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> showCartItems(@AuthPrincipal AuthMember authMember) {
        return ResponseEntity.ok(cartItemService.findByMember(authMember));
    }

    @PostMapping
    public ResponseEntity<Void> addCartItems(@AuthPrincipal AuthMember authMember, @Valid @RequestBody CartItemRequest cartItemRequest) {
        Long cartItemId = cartItemService.add(authMember, cartItemRequest);

        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCartItemQuantity(@AuthPrincipal AuthMember authMember, @PathVariable Long id,
                                                       @Valid @RequestBody CartItemQuantityUpdateRequest request) {
        cartItemService.updateQuantity(authMember, id, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItems(@AuthPrincipal AuthMember authMember, @PathVariable Long id) {
        cartItemService.remove(authMember, id);

        return ResponseEntity.noContent().build();
    }
}
