package cart.dto.request;

import javax.validation.constraints.Positive;

public class CartItemIdRequest {

    @Positive(message = "카트 아이템 ID는 0보다 커야합니다.")
    private Long cartItemId;

    public CartItemIdRequest() {
    }

    public CartItemIdRequest(final Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Long getCartItemId() {
        return cartItemId;
    }
}
