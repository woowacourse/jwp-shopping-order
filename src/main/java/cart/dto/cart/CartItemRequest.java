package cart.dto.cart;

import javax.validation.constraints.NotNull;

public class CartItemRequest {

    @NotNull(message = "장바구니 상품 아이디를 입력해야 합니다.")
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
