package cart.ui;

import cart.application.CartItemService;
import cart.domain.Member;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
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
@Tag(name = "장바구니 관련 API", description = "장바구니의 아이템을 관리하는 API 입니다.")
@SecurityRequirement(name = "basic")
public class CartItemApiController {

    private final CartItemService cartItemService;

    public CartItemApiController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    @Operation(summary = "멤버별 장바구니 아이템 조회")
    public ResponseEntity<List<CartItemResponse>> showCartItems(
            Member member
    ) {
        return ResponseEntity.ok(cartItemService.findByMember(member));
    }

    @PostMapping
    @Operation(summary = "장바구니에 아이템 추가")
    public ResponseEntity<Void> addCartItems(
            Member member,
            @RequestBody CartItemRequest cartItemRequest
    ) {
        Long cartItemId = cartItemService.save(member, cartItemRequest);

        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "장바구니 아이템 수량 변경")
    public ResponseEntity<Void> updateCartItemQuantity(
            Member member,
            @Parameter(description = "장바구니 아이템 ID", example = "1") @PathVariable Long id,
            @RequestBody CartItemQuantityUpdateRequest request
    ) {
        cartItemService.updateQuantity(member, id, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "장바구니 아이템 삭제")
    public ResponseEntity<Void> removeCartItems(
            Member member,
            @Parameter(description = "장바구니 아이템 ID", example = "1") @PathVariable Long id
    ) {
        cartItemService.deleteById(member, id);

        return ResponseEntity.noContent().build();
    }
}
