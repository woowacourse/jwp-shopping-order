package cart.ui;

import cart.application.CartItemService;
import cart.domain.Member;
import cart.dto.CartItemDeleteRequest;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
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
        return ResponseEntity.ok(cartItemService.findByMember(member.getId()));
    }

    @PostMapping
    public ResponseEntity<Void> addCartItems(final Member member,
                                             @RequestBody final CartItemRequest cartItemRequest) {
        final Long cartItemId = cartItemService.add(member.getId(), cartItemRequest);

        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCartItemQuantity(final Member member,
                                                       @PathVariable final long id,
                                                       @RequestBody final CartItemQuantityUpdateRequest request) {
        cartItemService.updateQuantity(member.getId(), id, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeCartItems(final Member member,
                                                @RequestBody final CartItemDeleteRequest cartItemDeleteRequest) {
        cartItemService.remove(member.getId(), cartItemDeleteRequest.getCartItemIds());

        return ResponseEntity.noContent().build();
    }
}
