package cart.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CartItemRequest {
    @NotNull(message = "상품 아이디는 필수 입력 값입니다.")
    @Positive(message = "상품 아이디는 1 이상이어야 합니다.")
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
