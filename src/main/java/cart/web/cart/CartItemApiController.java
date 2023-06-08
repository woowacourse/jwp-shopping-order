package cart.web.cart;

import cart.application.CartItemService;
import cart.domain.member.Member;
import cart.dto.cart.CartItemQuantityUpdateRequest;
import cart.dto.cart.CartItemRequest;
import cart.dto.cart.CartItemResponse;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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