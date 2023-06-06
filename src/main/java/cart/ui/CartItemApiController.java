package cart.ui;

import java.net.URI;
import java.util.List;

import cart.application.CartItemService;
import cart.domain.Member;
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

@RestController
@RequestMapping("/cart-items")
public class CartItemApiController {

    private final CartItemService cartItemService;

    public CartItemApiController(final CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping
    public ResponseEntity<CartItemResponse> addCartItem(final Member member, @RequestBody final CartItemRequest cartItemRequest) {
        final CartItemResponse cartItemResponse = cartItemService.addCartItem(member, cartItemRequest);
        return ResponseEntity.created(URI.create("/cart-items/" + cartItemResponse.getId())).body(cartItemResponse);
    }
    
    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCartItems(final Member member) {
        return ResponseEntity.ok(cartItemService.findByMember(member));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCartItemQuantity(final Member member, @PathVariable final Long id, @RequestBody final CartItemQuantityUpdateRequest request) {
        cartItemService.updateQuantity(member, id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCartItem(final Member member, @PathVariable final Long id) {
        cartItemService.deleteCartItem(member, id);
        return ResponseEntity.noContent().build();
    }
}
