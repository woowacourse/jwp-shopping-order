package cart.dto;

import javax.validation.constraints.NotNull;

public class CartItemRequest {

    @NotNull(message = "카트 아이템의 아이디를 입력해 주세요. 입력값 : ${validatedValue}")
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
