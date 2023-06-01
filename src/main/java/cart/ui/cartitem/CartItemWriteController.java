package cart.ui.cartitem;

import cart.application.service.cartitem.CartItemWriteService;
import cart.application.service.cartitem.dto.CartItemCreateDto;
import cart.application.service.cartitem.dto.CartItemUpdateDto;
import cart.ui.MemberAuth;
import cart.ui.cartitem.dto.CartItemQuantityUpdateRequest;
import cart.ui.cartitem.dto.CartItemRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/cart-items")
public class CartItemWriteController {

    private final CartItemWriteService cartItemWriteService;

    public CartItemWriteController(final CartItemWriteService cartItemWriteService) {
        this.cartItemWriteService = cartItemWriteService;
    }

    @PostMapping
    public ResponseEntity<Void> addCartItems(MemberAuth memberAuth, @RequestBody CartItemRequest cartItemRequest) {

        final Long cartItemId = cartItemWriteService.createCartItem(memberAuth, CartItemCreateDto.from(cartItemRequest));

        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCartItemQuantity(
            MemberAuth memberAuth,
            @PathVariable("id") Long cartItemId,
            @RequestBody CartItemQuantityUpdateRequest cartItemQuantityUpdateRequest
    ) {

        cartItemWriteService.updateQuantity(memberAuth, cartItemId, CartItemUpdateDto.from(cartItemQuantityUpdateRequest));

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItems(MemberAuth memberAuth, @PathVariable Long id) {
        cartItemWriteService.remove(memberAuth, id);

        return ResponseEntity.noContent().build();
    }
}
