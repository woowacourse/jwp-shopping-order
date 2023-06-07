package cart.ui;

import cart.domain.Member;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.service.CartItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/cart-items")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> showCartItems(Member member) {
        return ResponseEntity.ok(cartItemService.findByMember(member));
    }

    @PostMapping
    public ResponseEntity<Void> addCartItems(Member member, @RequestBody @Valid CartItemRequest cartItemRequest) {
        Long cartItemId = cartItemService.add(member, cartItemRequest);

        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCartItemQuantity(Member member, @PathVariable("id") @Min(value = 1, message = "ID는 1 이상의 정수로 입력해주세요") Long id,
                                                       @RequestBody @Valid CartItemQuantityUpdateRequest request) {
        cartItemService.updateQuantity(member, id, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItems(Member member,
                                                @PathVariable("id") @Min(value = 1, message = "ID는 1 이상의 정수로 입력해주세요") Long id) {
        cartItemService.remove(member, id);

        return ResponseEntity.noContent().build();
    }
}
