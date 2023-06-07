package cart.dto;

import javax.validation.constraints.NotNull;

public class CartItemRequest {

    @NotNull(message = "상품 id가 입력되지 않았습니다.")
    private Long productId;

    public CartItemRequest() {
    }

    public CartItemRequest(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
