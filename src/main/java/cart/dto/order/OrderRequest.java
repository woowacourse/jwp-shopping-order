package cart.dto.order;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class OrderRequest {

    @NotNull(message = "주문할 장바구니 상품의 ID를 1개 이상 입력해야 합니다.")
    private final List<Long> cartItemIds;

    @Positive(message = "주문 금액은 1이상의 값이어야 합니다.")
    private final int finalPrice;

    public OrderRequest(final List<Long> cartItemIds, final int finalPrice) {
        this.cartItemIds = cartItemIds;
        this.finalPrice = finalPrice;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public int getFinalPrice() {
        return finalPrice;
    }
}
