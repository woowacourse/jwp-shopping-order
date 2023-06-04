package cart.dto.order;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class OrderRequest {

    @NotEmpty(message = "주문할 상품은 비어있을 수 없습니다.")
    private List<Long> cartItemIds;
    @NotNull
    private int usePoint;

    public OrderRequest(List<Long> cartItemIds, int usePoint) {
        this.cartItemIds = cartItemIds;
        this.usePoint = usePoint;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public int getUsePoint() {
        return usePoint;
    }
}
