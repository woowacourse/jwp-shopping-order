package cart.ui;

import cart.application.CartItemService;
import cart.domain.Member;
import cart.dto.CartItemQuantityUpdateRequest;
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

    public CartItemApiController(final CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> showCartItems(final Member member) {
        return ResponseEntity.ok(this.cartItemService.findByMember(member));
    }

    @PostMapping
    public ResponseEntity<Void> addCartItems(final Member member, @RequestBody final CartItemRequest cartItemRequest) {
        final Long cartItemId = this.cartItemService.add(member, cartItemRequest);

        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCartItemQuantity(final Member member, @PathVariable final Long id,
                                                       @RequestBody final CartItemQuantityUpdateRequest request) {
        this.cartItemService.updateQuantity(member, id, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItems(final Member member, @PathVariable final Long id) {
        this.cartItemService.remove(member, id);

        return ResponseEntity.noContent().build();
    }
}