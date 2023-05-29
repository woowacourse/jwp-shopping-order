package cart.ui;

import cart.application.CartItemService;
import cart.domain.member.Member;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cart-items")
public class CartItemApiController {

    private final CartItemService cartItemService;

    public CartItemApiController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> showCartItems(final Member member) {
        final List<CartItemResponse> cartItemResponses = cartItemService.getCartItemsByMember(member)
                .stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cartItemResponses);
    }

    @PostMapping
    public ResponseEntity<Void> addCartItems(Member member,
                                             @RequestBody CartItemRequest request) {
        final Long cartItemId = cartItemService.addCartItems(member, request);
        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @PatchMapping("/{cartItemId}")
    public ResponseEntity<Void> updateCartItemQuantity(Member member,
                                                       @PathVariable Long cartItemId,
                                                       @RequestBody CartItemQuantityUpdateRequest request) {
        cartItemService.updateQuantity(member, cartItemId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeCartItems(Member member,
                                                @PathVariable Long cartItemId) {
        cartItemService.remove(member, cartItemId);
        return ResponseEntity.noContent().build();
    }
}
