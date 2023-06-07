package cart.ui.api;

import cart.application.CartItemService;
import cart.application.request.CreateCartItemRequest;
import cart.application.request.UpdateCartItemQuantityRequest;
import cart.application.response.CartItemResponse;
import cart.domain.member.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RequestMapping("/cart-items")
@RestController
public class CartItemApiController {

    private final CartItemService cartItemService;

    public CartItemApiController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> showCartItems(Member member) {
        List<CartItemResponse> findCartItemResponses = cartItemService.findByMember(member);

        return ResponseEntity
                .status(OK)
                .body(findCartItemResponses);
    }

    @PostMapping
    public ResponseEntity<Void> addCartItems(Member member, @Valid @RequestBody CreateCartItemRequest request) {
        Long cartItemId = cartItemService.addCartItem(member, request);

        return ResponseEntity
                .created(URI.create("/cart-items/" + cartItemId))
                .build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCartItemQuantity(
            Member member,
            @PathVariable(name = "id") @NotNull Long cartItemId,
            @Valid @RequestBody UpdateCartItemQuantityRequest request
    ) {
        cartItemService.updateQuantity(member, cartItemId, request);

        return ResponseEntity
                .ok()
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItems(Member member, @PathVariable(name = "id") @NotNull Long cartItemId) {
        cartItemService.remove(member, cartItemId);

        return ResponseEntity
                .noContent()
                .build();
    }
}
