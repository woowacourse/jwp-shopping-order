package cart.dto;

import javax.validation.constraints.NotNull;

public class CartItemSaveRequest {

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
