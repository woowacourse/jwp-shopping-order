package cart.dto.cartitem;

import javax.validation.constraints.Positive;

public class CartItemRequest {
    @Positive(message = "장바구니 상품의 productId는 1이상의 값으로 입력해야 합니다.")
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
