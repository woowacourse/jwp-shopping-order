package cart.dto.cartitem;

import javax.validation.constraints.NotNull;

public class CartItemRequest {

    @NotNull(message = "장바구니에 담을 id는 비어있을 수 없습니다.")
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
