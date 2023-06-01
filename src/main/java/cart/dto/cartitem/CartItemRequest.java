package cart.dto.cartitem;

import javax.validation.constraints.Positive;

public class CartItemRequest {

    @Positive(message = "상품 아이디가 잘못되었습니다.")
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
