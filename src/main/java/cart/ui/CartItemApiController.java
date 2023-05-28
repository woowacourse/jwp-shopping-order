package cart.ui;

import cart.application.CartItemService;
import cart.domain.Member;
import cart.dto.CartItemUpdateResponse;
import cart.dto.CartItemUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
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
    public ResponseEntity<List<CartItemResponse>> showCartItems(@Auth Member member) {
        return ResponseEntity.ok(cartItemService.findByMember(member));
    }

    @PostMapping
    public ResponseEntity<CartItemUpdateResponse> addCartItems(@Auth Member member, @RequestBody CartItemRequest cartItemRequest) {
        Long cartItemId = cartItemService.add(member, cartItemRequest);
        final CartItemResponse response = cartItemService.findById(cartItemId);
        final CartItemUpdateResponse createdResponse = new CartItemUpdateResponse(response.getQuantity(), response.isChecked());
        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).body(createdResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CartItemUpdateResponse> updateCartItem(@Auth Member member, @PathVariable Long id, @RequestBody CartItemUpdateRequest request) {
        CartItemUpdateResponse cartItemUpdateResponse = cartItemService.updateQuantity(member, id, request);

        return ResponseEntity.ok(cartItemUpdateResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItems(@Auth Member member, @PathVariable Long id) {
        cartItemService.remove(member, id);

        return ResponseEntity.noContent().build();
    }
}
