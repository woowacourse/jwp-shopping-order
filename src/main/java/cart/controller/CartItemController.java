package cart.controller;

import cart.auth.Auth;
import cart.auth.Credential;
import cart.dto.cart.CartItemQuantityUpdateRequest;
import cart.dto.cart.CartItemResponse;
import cart.dto.cart.CartItemSaveRequest;
import cart.exception.common.ExceptionResponse;
import cart.service.CartItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
import org.springframework.web.bind.annotation.RestController;

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
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "장바구니 상품 추가 성공."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "장바구니 상품 추가 실패.",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<Void> save(
            @Auth final Credential credential,
            @Valid @RequestBody final CartItemSaveRequest request
    ) {
        final Long id = cartItemService.save(credential.getMemberId(), request);
        return ResponseEntity.created(URI.create("/cart-items/" + id)).build();
    }

    @Operation(summary = "장바구니 상품 전체 조회", description = "장바구니 상품 전체를 조회한다.")
    @ApiResponse(
            responseCode = "200",
            description = "장바구니 상품 전체 조회 성공"
    )
    @GetMapping
    public ResponseEntity<List<CartItemResponse>> findAll(@Auth final Credential credential) {
        final List<CartItemResponse> result = cartItemService.findAll(credential.getMemberId());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "장바구니 상품 수량 수정", description = "장바구니 상품의 수량을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "장바구니 상품 수량 수정 성공."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "장바구니 상품 수량 수정 실패.",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
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
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "장바구니 상품 삭제 성공."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "장바구니 상품 삭제 실패.",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    @DeleteMapping("{cartItemId}")
    public ResponseEntity<Void> delete(
            @Auth final Credential credential,
            @Parameter(description = "장바구니 아이템 Id") @PathVariable final Long cartItemId
    ) {
        cartItemService.delete(cartItemId, credential.getMemberId());
        return ResponseEntity.noContent().build();
    }
}
