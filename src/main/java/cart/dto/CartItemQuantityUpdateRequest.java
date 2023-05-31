package cart.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public class CartItemQuantityUpdateRequest {

    @NotNull(message = "수량을 입력하세요")
    @PositiveOrZero(message = "수량은 0개 이상만 가능합니다. (0개: 삭제)")
    private Integer quantity;

    private CartItemQuantityUpdateRequest(){
    }

    private CartItemQuantityUpdateRequest(final Integer quantity) {
        this.quantity = quantity;
    }

    public static CartItemQuantityUpdateRequest from(final Integer quantity) {
        return new CartItemQuantityUpdateRequest(quantity);
    }

    public Integer getQuantity() {
        return quantity;
    }
}
