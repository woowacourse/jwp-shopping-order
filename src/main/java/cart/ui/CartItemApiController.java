package cart.ui;

import cart.application.CartItemService;
import cart.application.dto.request.CartItemQuantityUpdateRequest;
import cart.application.dto.request.CartItemRequest;
import cart.application.dto.response.CartItemResponse;
import cart.domain.member.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/cart-items")
public class CartItemApiController {

    private final CartItemService cartItemService;

    public CartItemApiController(final CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> showCartItems(final Member member) {
        return ResponseEntity.ok(cartItemService.findAllByMember(member));
    }

    @PostMapping
    public ResponseEntity<Void> addCartItem(final Member member, @RequestBody final CartItemRequest cartItemRequest) {
        final Long cartItemId = cartItemService.create(member, cartItemRequest);

        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<Void> updateCartItemQuantity(final Member member, @PathVariable final Long productId, @RequestBody final CartItemQuantityUpdateRequest request) {
        cartItemService.update(member, productId, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeCartItem(final Member member, @PathVariable final Long productId) {
        cartItemService.delete(member, productId);

        return ResponseEntity.noContent().build();
    }
}
