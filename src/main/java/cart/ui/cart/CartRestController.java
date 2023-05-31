package cart.ui.cart;

import cart.application.CartItemService;
import cart.domain.member.Member;
import cart.ui.cart.dto.request.CartItemQuantityUpdateRequest;
import cart.ui.cart.dto.request.CartItemRequest;
import cart.ui.cart.dto.response.CartItemResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cart-items")
public class CartRestController {
    private final CartItemService cartItemService;

    public CartRestController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> showCartItems(Member member) {
        List<CartItemResponse> responses = cartItemService.findByMember(member).stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<Void> addCartItems(Member member, @RequestBody CartItemRequest cartItemRequest) {
        Long cartItemId = cartItemService.add(member, cartItemRequest.getProductId());

        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCartItemQuantity(Member member, @PathVariable Long id,
                                                       @RequestBody CartItemQuantityUpdateRequest request) {
        cartItemService.updateQuantity(member, id, request.getQuantity());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItems(Member member, @PathVariable Long id) {
        cartItemService.remove(member, id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeCartItems(Member member, @RequestParam List<Long> ids) {
        cartItemService.removeItems(member, ids);

        return ResponseEntity.noContent().build();
    }
}
