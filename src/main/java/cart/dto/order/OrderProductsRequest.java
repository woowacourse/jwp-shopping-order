package cart.dto.order;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;

public class OrderProductsRequest {

    @NotNull
    @Size(min = 1, message = "개수는 최소 1개 이상이어야 합니다. value=${validatedValue}")
    private List<Long> cartIds;
    @PositiveOrZero(message = "입력 가능한 포인트는 0포인트 부터입니다.")
    private int point;
    @PositiveOrZero(message = "입력 가능한 배달비는 0원 부터입니다.")
    private int deliveryFee;

    public OrderProductsRequest() {
    }

    public OrderProductsRequest(List<Long> cartIds, int point, int deliveryFee) {
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

    @Override
    public String toString() {
        return "OrderProductsRequest{" +
                "cartIds=" + cartIds +
                ", point=" + point +
                '}';
    }
}
