package cart.ui;

import cart.application.CartItemService;
import cart.domain.Member;
import cart.dto.CartItemAddRequest;
import cart.dto.CartItemResponse;
import cart.dto.CartItemUpdateRequest;
import cart.dto.CartItemUpdateResponse;
import io.swagger.v3.oas.annotations.Operation;

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

    @Operation(summary = "장바구니 상품 추가")
    @PostMapping
    public ResponseEntity<CartItemUpdateResponse> addCartItems(@Auth Member member, @RequestBody CartItemAddRequest cartItemAddRequest) {
        Long cartItemId = cartItemService.add(member, cartItemAddRequest);
        final CartItemResponse response = cartItemService.findById(cartItemId);
        final CartItemUpdateResponse createdResponse = new CartItemUpdateResponse(response.getQuantity(), response.isChecked());
        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).body(createdResponse);
    }

    @Operation(summary = "장바구니 상품 조회")
    @GetMapping
    public ResponseEntity<List<CartItemResponse>> showCartItems(@Auth Member member) {
        return ResponseEntity.ok(cartItemService.findByMember(member));
    }

    @Operation(summary = "장바구니 상품 수정")
    @PatchMapping("/{id}")
    public ResponseEntity<CartItemUpdateResponse> updateCartItem(@Auth Member member, @PathVariable Long id, @RequestBody CartItemUpdateRequest request) {
        CartItemUpdateResponse cartItemUpdateResponse = cartItemService.updateQuantity(member, id, request);

        return ResponseEntity.ok(cartItemUpdateResponse);
    }

    @Operation(summary = "장바구니 상품 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItems(@Auth Member member, @PathVariable Long id) {
        cartItemService.remove(member, id);

        return ResponseEntity.noContent().build();
    }
}
