package cart.dto.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;

@Schema(description = "장바구니 상품 추가 Request")
public class CartItemSaveRequest {

    @Schema(description = "상품 Id", example = "1")
    @NotNull(message = "상품 Id를 입력해야 합니다")
    private Long productId;

    public CartItemSaveRequest() {
    }

    public CartItemSaveRequest(final Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
