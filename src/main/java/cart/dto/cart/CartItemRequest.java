package cart.dto.cart;

import javax.validation.constraints.NotNull;

public class CartItemRequest {

    @NotNull(message = "상품의 id값을 입력해주세요.")
    private Long productId;

    public CartItemRequest() {
    }

    public CartItemRequest(final Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
