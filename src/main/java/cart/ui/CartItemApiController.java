package cart.ui;

import cart.application.CartItemService;
import cart.domain.Member;
import cart.dto.request.CartItemQuantityUpdateRequest;
import cart.dto.request.CartItemRequest;
import cart.dto.response.CartItemResponse;
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
    public ResponseEntity<List<CartItemResponse>> findByMemberId(Member member) {
        return ResponseEntity.ok(cartItemService.findByMemberId(member));
    }

    @PostMapping
    public ResponseEntity<Void> save(Member member, @RequestBody CartItemRequest cartItemRequest) {
        Long cartItemId = cartItemService.save(member, cartItemRequest);

        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateQuantityById(Member member, @PathVariable Long id, @RequestBody CartItemQuantityUpdateRequest request) {
        cartItemService.updateQuantity(member, id, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(Member member, @PathVariable Long id) {
        cartItemService.deleteById(member, id);

        return ResponseEntity.noContent().build();
    }
}
