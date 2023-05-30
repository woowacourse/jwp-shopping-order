package cart.application.dto.cartitem;

import javax.validation.constraints.NotNull;

public class CartItemRequest {

    @NotNull(message = "상품 아이디는 비어있을 수 없습니다.")
    private final Long productId;

    public CartItemRequest() {
        this(null);
    }

    public CartItemRequest(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
