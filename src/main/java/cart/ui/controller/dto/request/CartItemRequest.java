package cart.ui.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;

@Schema(description = "장바구니 상품 등록 요청")
public class CartItemRequest {

    @Schema(description = "장바구니에 등록될 상품 ID", example = "1")
    @NotNull(message = "장바구니 등록을 위해 상품 ID는 필수입니다.")
    private Long productId;

    private CartItemRequest() {
    }

    public CartItemRequest(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
