package cart.presentation.controller;

import cart.application.service.CartItemService;
import cart.presentation.dto.request.AuthInfo;
import cart.presentation.dto.request.CartItemQuantityRequest;
import cart.presentation.dto.request.CartItemRequest;
import cart.presentation.dto.response.CartItemResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/cart-items")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> showCartItems(AuthInfo authInfo) {
        List<CartItemResponse> cartItemResponses = cartItemService.findAllCartItems(authInfo);
        return ResponseEntity.ok(cartItemResponses);
    }

    @PostMapping
    public ResponseEntity<Void> addCartItems(AuthInfo authInfo, @RequestBody CartItemRequest request) {
        Long cartItemId = cartItemService.createCartItem(authInfo, request);
        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCartItemQuantity(AuthInfo authInfo, @PathVariable Long id,
                                                       @RequestBody CartItemQuantityRequest request) {
        cartItemService.updateQuantity(authInfo, id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItems(AuthInfo authInfo, @PathVariable Long id) {
        cartItemService.remove(authInfo, id);
        return ResponseEntity.noContent().build();
    }
}
