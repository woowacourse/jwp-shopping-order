package cart.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;

public class CartItemsDeleteRequest {
    @NotEmpty(message = "삭제할 장바구니 아이디 리스트는 비어 있을 수 없습니다.")
    private List<Long> cartItemIds;

    public CartItemsDeleteRequest() {
    }

    public CartItemsDeleteRequest(final List<Long> cartItemIds) {
        this.cartItemIds = cartItemIds;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }
}
