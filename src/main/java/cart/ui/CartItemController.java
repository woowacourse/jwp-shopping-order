package cart.ui;

import cart.application.CartItemService;
import cart.application.dto.CartItemQuantityUpdateRequest;
import cart.application.dto.CartItemRequest;
import cart.application.dto.CartItemResponse;
import cart.common.auth.MemberName;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart-items")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> showCartItems(@MemberName String memberName) {
        return ResponseEntity.ok(cartItemService.findByMember(memberName));
    }

    @PostMapping
    public ResponseEntity<Void> addCartItems(@MemberName String memberName,
                                             @RequestBody @Valid CartItemRequest cartItemRequest) {
        final long cartItemId = cartItemService.addCart(memberName, cartItemRequest);
        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCartItemQuantity(@MemberName String memberName,
                                                       @PathVariable Long id,
                                                       @RequestBody CartItemQuantityUpdateRequest updateRequest) {
        cartItemService.updateQuantity(memberName, id, updateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItem(@MemberName String memberName,
                                               @PathVariable Long id) {
        cartItemService.remove(memberName, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeCartItems(@MemberName String memberName,
                                                @RequestParam List<Long> ids) {
        cartItemService.removeItems(memberName, ids);
        return ResponseEntity.noContent().build();
    }
}
