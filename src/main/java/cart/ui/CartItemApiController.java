package cart.ui;

import cart.application.CartItemService;
import cart.domain.Member;
import cart.dto.request.CartItemQuantityUpdateRequest;
import cart.dto.request.CartItemRequest;
import cart.dto.response.CartItemResponse;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

    public CartItemApiController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping
    public ResponseEntity<Void> cartItemSave(Member member,
                                             @Validated @RequestBody CartItemRequest cartItemRequest) {
        Long cartItemId = cartItemService.save(member, cartItemRequest);
        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> cartItemList(Member member) {
        return ResponseEntity.ok(cartItemService.findByMember(member));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> cartItemUpdate(Member member,
                                               @PathVariable Long id,
                                               @Validated @RequestBody CartItemQuantityUpdateRequest request) {
        cartItemService.updateQuantity(member, id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cartItemDelete(Member member, @PathVariable Long id) {
        cartItemService.deleteById(member, id);
        return ResponseEntity.noContent().build();
    }
}
