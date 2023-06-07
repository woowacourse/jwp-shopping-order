package cart.application.request;

import javax.validation.constraints.NotNull;

public class CreateCartItemRequest {

    @NotNull(message = "장바구니 상품 아이디를 입력해야 합니다.")
    private Long productId;

    public CreateCartItemRequest() {
    }

    public CreateCartItemRequest(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
