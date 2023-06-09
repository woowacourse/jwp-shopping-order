package cart.ui.dto.request;

import javax.validation.constraints.NotNull;

public class CreateCartItemRequest {

    @NotNull(message = "상품 id가 입력되지 않았습니다.")
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
