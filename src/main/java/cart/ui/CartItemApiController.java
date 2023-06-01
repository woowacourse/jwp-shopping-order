package cart.ui;

import cart.application.CartItemService;
import cart.domain.Member;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "cart-item", description = "장바구니 API")
@SecurityRequirement(name = "basicAuth")
@RestController
@RequestMapping("/cart-items")
public class CartItemApiController {

    private final CartItemService cartItemService;

    public CartItemApiController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }


    @Operation(
            summary = "장바구니 조회",
            description = "장바구니를 조회한다.",
            responses = {
                    @ApiResponse(description = "장바구니 조회 성공", responseCode = "200"),
                    @ApiResponse(description = "인증에러", responseCode = "401")
            }
    )
    @GetMapping
    public ResponseEntity<List<CartItemResponse>> showCartItems(@Parameter(name = "member", description = "인증된 멤버", required = true, hidden = true) Member member) {
        return ResponseEntity.ok(cartItemService.findByMember(member));
    }

    @Operation(
            summary = "장바구니 추가",
            description = "장바구니를 추가한다.",
            responses = {
                    @ApiResponse(description = "장바구니 추가 성공", responseCode = "201", headers = {@Header(name = "Location")}),
                    @ApiResponse(description = "인증에러", responseCode = "401")
            }
    )
    @PostMapping
    public ResponseEntity<Void> addCartItems(@Parameter(name = "member", description = "인증된 멤버", required = true, hidden = true) Member member,
                                             @RequestBody CartItemRequest cartItemRequest) {
        Long cartItemId = cartItemService.add(member, cartItemRequest);

        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @Operation(
            summary = "장바구니 수량 갱신",
            description = "장바구니 수량을 갱신한다.",
            responses = {
                    @ApiResponse(description = "장바구니 갱신 성공", responseCode = "200"),
                    @ApiResponse(description = "인증에러", responseCode = "401"),
                    @ApiResponse(description = "인가에러", responseCode = "403")
            }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCartItemQuantity(@Parameter(name = "member", description = "인증된 멤버", required = true, hidden = true) Member member,
                                                       @Parameter(description = "장바구니 id", required = true) @PathVariable Long id,
                                                       @RequestBody CartItemQuantityUpdateRequest request) {
        cartItemService.updateQuantity(member, id, request);

        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "장바구니 제거",
            description = "장바구니를 제거한다.",
            responses = {
                    @ApiResponse(description = "장바구니 제거 성공", responseCode = "204"),
                    @ApiResponse(description = "인증에러", responseCode = "401"),
                    @ApiResponse(description = "인가에러", responseCode = "403")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItems(@Parameter(name = "member", description = "인증된 멤버", required = true, hidden = true) Member member,
                                                @Parameter(description = "장바구니 id", required = true) @PathVariable Long id) {
        cartItemService.remove(member, id);

        return ResponseEntity.noContent().build();
    }
}
