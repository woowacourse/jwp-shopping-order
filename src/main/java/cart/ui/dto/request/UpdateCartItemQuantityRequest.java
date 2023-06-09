package cart.ui.dto.request;

import javax.validation.constraints.Positive;

public class UpdateCartItemQuantityRequest {

    @Positive(message = "업데이트 수량은 음수일 수 없습니다.")
    private Integer quantity;

    public UpdateCartItemQuantityRequest() {
    }

    public UpdateCartItemQuantityRequest(Integer quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}
