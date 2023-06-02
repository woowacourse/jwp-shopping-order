package cart.controller;

import cart.auth.Auth;
import cart.domain.member.Member;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
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
public class CartItemApiController {

    private final CartItemService cartItemService;

    public CartItemApiController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> showCartItems(@Auth final Member member) {
        return ResponseEntity.ok(cartItemService.findByMember(member));
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<CartItemResponse> showCartItemByProductId(@Auth final Member member, @PathVariable final Long productId) {
        return ResponseEntity.ok(cartItemService.findByProductId(member, productId));
    }

    @PostMapping
    public ResponseEntity<Void> addCartItems(@Auth final Member member, @RequestBody final CartItemRequest cartItemRequest) {
        Long cartItemId = cartItemService.add(member, cartItemRequest);

        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCartItemQuantity(@Auth final Member member, @PathVariable final Long id, @RequestBody final CartItemQuantityUpdateRequest request) {
        cartItemService.updateQuantity(member, id, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItems(@Auth final Member member, @PathVariable final Long id) {
        cartItemService.remove(member, id);

        return ResponseEntity.noContent().build();
    }
}
