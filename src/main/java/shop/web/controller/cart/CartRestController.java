package shop.web.controller.cart;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.application.cart.CartItemService;
import shop.application.cart.dto.CartDto;
import shop.domain.member.Member;
import shop.web.controller.cart.dto.request.CartItemRequest;
import shop.web.controller.cart.dto.request.CartQuantityUpdateRequest;
import shop.web.controller.cart.dto.response.CartResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/cart-items")
public class CartRestController {
    private final CartItemService cartItemService;

    public CartRestController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public ResponseEntity<List<CartResponse>> showCartItems(Member member) {
        List<CartDto> cartDtos = cartItemService.findByMember(member);
        List<CartResponse> responses = CartResponse.of(cartDtos);

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<Void> addCartItems(Member member, @RequestBody CartItemRequest request) {
        Long cartItemId = cartItemService.add(member, request.getProductId());

        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCartItemQuantity(Member member, @PathVariable Long id,
                                                       @RequestBody CartQuantityUpdateRequest request) {
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
