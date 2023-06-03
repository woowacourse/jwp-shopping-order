package cart.application.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public class OrderRequest {

    @NotNull(message = "주문할 장바구니 목록이 null일 수 없습니다.")
    @NotEmpty(message = "주문할 장바구니 목록이 비어있을 수 없습니다.")
    private List<Long> cartIds;

    @PositiveOrZero(message = "사용할 포인트는 음수일 수 없습니다.")
    private int point;
    private int deliveryFee;

    public OrderRequest() {
    }

    public OrderRequest(List<Long> cartIds, int point, int deliveryFee) {
        this.cartIds = cartIds;
        this.point = point;
        this.deliveryFee = deliveryFee;
    }

    public List<Long> getCartIds() {
        return cartIds;
    }

    public int getPoint() {
        return point;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }
}
