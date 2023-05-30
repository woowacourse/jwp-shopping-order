package cart.dto.request;

import java.util.List;
import javax.validation.constraints.NotEmpty;

public class OrderRequest {

    @NotEmpty(message = "장바구니 id가 입력되지 않았습니다.")
    private final List<Long> cartItemIds;

    public OrderRequest(final List<Long> cartItemIds) {
        this.cartItemIds = cartItemIds;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }
}
