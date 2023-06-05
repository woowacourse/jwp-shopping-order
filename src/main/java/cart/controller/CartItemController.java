package cart.controller;

import cart.auth.Auth;
import cart.auth.Credential;
import cart.domain.CartItem;
import cart.dto.CartItemDto;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemSaveRequest;
import cart.service.CartItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@SecurityRequirement(name = "basicAuth")
@Tag(name = "장바구니", description = "장바구니를 관리한다.")
@RequestMapping("/cart-items")
@RestController
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(final CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @Operation(summary = "장바구니 상품 추가", description = "장바구니에 상품을 추가한다.")
    @PostMapping
    public ResponseEntity<Void> save(
            @Auth final Credential credential,
            @RequestBody final CartItemSaveRequest request
    ) {
        final CartItem cartItem = cartItemService.save(credential.getMemberId(), request);
        return ResponseEntity.created(URI.create("/cart-items/" + cartItem.getId())).build();
    }

    @Operation(summary = "장바구니 상품 전체 조회", description = "장바구니 상품 전체를 조회한다.")
    @GetMapping
    public ResponseEntity<List<CartItemDto>> findAll(@Auth final Credential credential) {
        final List<CartItem> cartItems = cartItemService.findAll(credential.getMemberId());
        final List<CartItemDto> result = cartItems.stream()
                .map(CartItemDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "장바구니 상품 수량 수정", description = "장바구니 상품의 수량을 수정한다.")
    @PatchMapping("/{cartItemId}")
    public ResponseEntity<Void> updateQuantity(
            @Auth final Credential credential,
            @Parameter(description = "장바구니 아이템 Id") @PathVariable final Long cartItemId,
            @Valid @RequestBody final CartItemQuantityUpdateRequest request
    ) {
        cartItemService.updateQuantity(credential.getMemberId(), cartItemId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "장바구니 상품 삭제", description = "장바구니 상품을 삭제한다.")
    @DeleteMapping("{cartItemId}")
    public ResponseEntity<Void> delete(
            @Auth final Credential credential,
            @Parameter(description = "장바구니 아이템 Id") @PathVariable final Long cartItemId
    ) {
        cartItemService.delete(cartItemId, credential.getMemberId());
        return ResponseEntity.noContent().build();
    }
}
