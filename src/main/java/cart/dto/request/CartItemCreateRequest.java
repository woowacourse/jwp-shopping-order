package cart.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CartItemCreateRequest {

    @NotNull(message = "카트 아이템의 아이디를 입력해 주세요. 입력값 : ${validatedValue}")
    private final Long productId;

    @NotNull(message = "카트 아이템의 수량을 입력해 주세요. 입력값 : ${validatedValue}")
    @Positive(message = "카트 아이템의 수량은 1 이상으로 입력해 주세요. 입력값 : ${validatedValue}")
    private final int quantity;

    public CartItemCreateRequest(Long productId, final int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
