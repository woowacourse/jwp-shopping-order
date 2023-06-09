package cart.ui.dto.request;

import java.util.List;
import javax.validation.constraints.NotEmpty;

public class CreateOrderRequest {

    @NotEmpty(message = "장바구니 id가 입력되지 않았습니다.")
    private List<Long> cartItemIds;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(final List<Long> cartItemIds) {
        this.cartItemIds = cartItemIds;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }
}
