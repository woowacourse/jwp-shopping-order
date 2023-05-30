package cart.ui;

import cart.application.CartService;
import cart.application.dto.cartitem.CartItemQuantityUpdateRequest;
import cart.application.dto.cartitem.CartItemRequest;
import cart.application.dto.cartitem.CartResponse;
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
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<List<CartResponse>> showCartItems(@MemberName String memberName) {
        return ResponseEntity.ok(cartService.findByMember(memberName));
    }

    @PostMapping
    public ResponseEntity<Void> addCartItems(@MemberName String memberName,
                                             @RequestBody @Valid CartItemRequest cartItemRequest) {
        final long cartItemId = cartService.addCart(memberName, cartItemRequest);
        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCartItemQuantity(@MemberName String memberName,
                                                       @PathVariable Long id,
                                                       @RequestBody CartItemQuantityUpdateRequest updateRequest) {
        cartService.updateQuantity(memberName, id, updateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItem(@MemberName String memberName,
                                               @PathVariable Long id) {
        cartService.remove(memberName, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeCartItems(@MemberName String memberName,
                                                @RequestParam List<Long> ids) {
        cartService.removeItems(memberName, ids);
        return ResponseEntity.noContent().build();
    }
}
