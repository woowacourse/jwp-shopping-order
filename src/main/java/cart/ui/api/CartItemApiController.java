package cart.ui.api;

import cart.application.CartItemService;
import cart.domain.Member;
import cart.dto.cartitem.CartItemQuantityUpdateRequest;
import cart.dto.cartitem.CartItemRequest;
import cart.dto.cartitem.CartItemsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "장바구니 상품(CartItem)", description = "장바구니 상품에 대한 api 입니다.")
@RestController
@RequestMapping("/cart-items")
public class CartItemApiController {

    private final CartItemService cartItemService;

    public CartItemApiController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @Operation(summary = "장바구니 상품 전체 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = CartItemsResponse.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")})
    @GetMapping
    public ResponseEntity<CartItemsResponse> showCartItems(@Parameter(description = "사용자 정보") Member member,
                                                           @Parameter(description = "페이지에 표시될 상품 수") @RequestParam("unit-size") int size,
                                                           @Parameter(description = "조회할 페이지") @RequestParam("page") int page) {
        CartItemsResponse cartItemsResponse = cartItemService.findCartItems(member, size, page);

        return ResponseEntity.ok().body(cartItemsResponse);
    }

    @Operation(summary = "장바구니 상품 추가")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED", headers = @Header(name = "Location", description = "cart-items/추가된 장바구니 상품 ID")),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")})
    @PostMapping
    public ResponseEntity<Void> addCartItems(@Parameter(description = "사용자 정보") Member member,
                                             @Parameter(description = "추가할 장바구니 아이템 정보") @RequestBody @Valid CartItemRequest cartItemRequest) {
        Long cartItemId = cartItemService.add(member, cartItemRequest);

        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @Operation(summary = "장바구니 상품 수량 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")})
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCartItemQuantity(@Parameter(description = "사용자 정보") Member member,
                                                       @Parameter(description = "수정할 장바구니 상품의 ID") @PathVariable Long id,
                                                       @Parameter(description = "수정할 장바구니 상품 수량 정보") @RequestBody @Valid CartItemQuantityUpdateRequest request) {
        cartItemService.updateQuantity(member, id, request);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "장바구니 상품 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "203", description = "NO CONTENT"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItems(@Parameter(description = "사용자 정보") Member member,
                                                @Parameter(description = "삭제할 장바구니 상품 ID") @PathVariable Long id) {
        cartItemService.remove(member, id);

        return ResponseEntity.noContent().build();
    }
}
