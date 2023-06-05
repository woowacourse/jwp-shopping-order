package cart.dto.cartitem;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.PositiveOrZero;


public class CartItemQuantityUpdateRequest {

    @PositiveOrZero(message = "장바구니 아이템의 수량은 0개 이상이어야합니다.")
    private long quantity;

    @JsonCreator
    public CartItemQuantityUpdateRequest(long quantity) {
        this.quantity = quantity;
    }

    public long getQuantity() {
        return quantity;
    }
}
