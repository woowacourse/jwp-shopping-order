package cart.dto.cart;

import javax.validation.constraints.NotBlank;

public class CartItemRequest {

    @NotBlank(message = "장바구니에 상품 요청시 상품이 존재해야 합니다.")
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
