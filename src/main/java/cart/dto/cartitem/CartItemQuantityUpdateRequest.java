package cart.dto.cartitem;

import javax.validation.constraints.PositiveOrZero;

public class CartItemQuantityUpdateRequest {

    @PositiveOrZero(message = "장바구니 아이템의 수량은 0개 이상이어야합니다.")
    private long quantity;

    public CartItemQuantityUpdateRequest() {
    }

    public CartItemQuantityUpdateRequest(long quantity) {
        this.quantity = quantity;
    }

    public long getQuantity() {
        return quantity;
    }
}
