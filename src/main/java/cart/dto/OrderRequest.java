package cart.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;

public class OrderRequest {

    @NotEmpty(message = "주문할 장바구니 상품이 존재해야 합니다.")
    private List<Long> cartItemIds;
    @PositiveOrZero(message = "사용할 포인트는 양수여야 합니다.")
    private Integer point;

    private OrderRequest() {
    }

    public OrderRequest(final List<Long> cartItemIds, final Integer point) {
        this.cartItemIds = cartItemIds;
        this.point = point;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public Integer getPoint() {
        return point;
    }
}
