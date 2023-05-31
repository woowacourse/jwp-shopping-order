package shop.ui.cart;

import shop.application.cart.CartItemService;
import shop.domain.member.Member;
import shop.ui.cart.dto.CartQuantityUpdateDto;
import shop.ui.cart.dto.CartItemRequest;
import shop.application.cart.dto.CartDto;
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
    public ResponseEntity<List<CartDto>> showCartItems(Member member) {
        List<CartDto> responses = cartItemService.findByMember(member).stream()
                .map(CartDto::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<Void> addCartItems(Member member, @RequestBody CartItemRequest request) {
        Long cartItemId = cartItemService.add(member, request.getProductId());

        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCartItemQuantity(Member member, @PathVariable Long id,
                                                       @RequestBody CartQuantityUpdateDto request) {
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
