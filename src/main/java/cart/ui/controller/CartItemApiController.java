package cart.ui.controller;

import cart.application.CartItemService;
import cart.domain.member.Member;
import cart.ui.controller.dto.request.CartItemQuantityUpdateRequest;
import cart.ui.controller.dto.request.CartItemRemoveRequest;
import cart.ui.controller.dto.request.CartItemRequest;
import cart.ui.controller.dto.response.CartItemPriceResponse;
import cart.ui.controller.dto.response.CartItemResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
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

@SecurityRequirement(name = "basic")
@Tag(name = "장바구니", description = "장바구니 API")
@RequestMapping("/cart-items")
@RestController
public class CartItemApiController {

    private final CartItemService cartItemService;

    public CartItemApiController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @Operation(summary = "장바구니 상품 목록 조회", description = "장바구니 상품 목록을 조회한다.")
    @ApiResponse(responseCode = "200", description = "장바구니 상품 목록 조회 성공")
    @GetMapping
    public ResponseEntity<List<CartItemResponse>> showCartItems(Member member) {
        return ResponseEntity.ok(cartItemService.findByMember(member));
    }

    @Operation(summary = "장바구니 상품 목록 금액 조회", description = "장바구니 상품 목록 금액을 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "장바구니 상품 목록 금액 조회 성공"),
            @ApiResponse(responseCode = "400", description = "장바구니 상품 목록 금액 조회 실패"),
            @ApiResponse(responseCode = "404", description = "등록되지 않은 데이터(장바구니 상품) 요청")
    })
    @GetMapping("/price")
    public ResponseEntity<CartItemPriceResponse> getTotalPriceWithDeliveryFee(
            Member member,
            @RequestParam("item") List<Long> cartItemIds
    ) {
        CartItemPriceResponse response = cartItemService.getTotalPriceWithDeliveryFee(member, cartItemIds);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "장바구니 상품 추가", description = "장바구니에 상품을 추가한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "장바구니 상품 추가 성공"),
            @ApiResponse(responseCode = "400", description = "장바구니 상품 추가 실패"),
            @ApiResponse(responseCode = "404", description = "등록되지 않은 데이터(상품) 요청")
    })
    @PostMapping
    public ResponseEntity<Void> addCartItems(Member member, @RequestBody @Valid CartItemRequest cartItemRequest) {
        Long cartItemId = cartItemService.add(member, cartItemRequest);
        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @Operation(summary = "장바구니 상품 수량 수정", description = "장바구니에 있는 상품의 수량을 수정한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "장바구니 상품 수량 수정 성공"),
            @ApiResponse(responseCode = "400", description = "장바구니 상품 수량 수정 실패"),
            @ApiResponse(responseCode = "404", description = "등록되지 않은 데이터(장바구니 상품) 요청")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCartItemQuantity(
            Member member,
            @PathVariable Long id,
            @RequestBody @Valid CartItemQuantityUpdateRequest request
    ) {
        cartItemService.updateQuantity(member, id, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "장바구니 상품 목록 삭제", description = "장바구니 상품 목록을 삭제한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "장바구니 상품 목록 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "장바구니 상품 목록 삭제 실패"),
            @ApiResponse(responseCode = "404", description = "등록되지 않은 데이터(장바구니 상품) 요청")
    })
    @DeleteMapping
    public ResponseEntity<Void> removeCartItems(Member member, @RequestBody @Valid CartItemRemoveRequest cartItemRemoveRequest) {
        cartItemService.removeCartItems(member, cartItemRemoveRequest);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "장바구니 상품 삭제", description = "장바구니에 있는 상품을 삭제한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "장바구니 상품 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "장바구니 상품 삭제 실패"),
            @ApiResponse(responseCode = "404", description = "등록되지 않은 데이터(장바구니 상품) 요청")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItem(Member member, @PathVariable Long id) {
        cartItemService.remove(member, id);
        return ResponseEntity.noContent().build();
    }
}
