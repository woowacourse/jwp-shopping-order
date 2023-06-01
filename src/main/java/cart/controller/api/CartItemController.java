package cart.controller.api;

import cart.auth.Auth;
import cart.controller.dto.request.CartItemQuantityUpdateRequest;
import cart.controller.dto.request.CartItemRequest;
import cart.controller.dto.response.CartItemResponse;
import cart.domain.Member;
import cart.service.CartItemService;
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

@RestController
@RequestMapping("/cart-items")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(final CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> showMembersCartItem(@Auth final Member member) {
        return ResponseEntity.ok(cartItemService.findByMember(member));
    }

    @PostMapping
    public ResponseEntity<Void> add(@Auth final Member member, @RequestBody final CartItemRequest cartItemRequest) {
        final long id = cartItemService.save(member, cartItemRequest);
        return ResponseEntity.created(URI.create("/cart-items/" + id)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateQuantity(
            @Auth final Member member, @PathVariable final Long id, @RequestBody final CartItemQuantityUpdateRequest request) {
        cartItemService.updateQuantity(member, id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@Auth final Member member, @PathVariable final Long id) {
        cartItemService.remove(member, id);
        return ResponseEntity.noContent().build();
    }
}
