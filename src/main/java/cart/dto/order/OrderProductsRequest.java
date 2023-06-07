package cart.dto.order;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class OrderProductsRequest {

    @NotNull
    @Size(min = 1, message = "1개 이상의 값을 입력해야 합니다. value=${validatedValue}")
    private List<Long> cartIds;
    private int point;
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

    public int getDeliveryFee() {
        return deliveryFee;
    }

    @Override
    public String toString() {
        return "OrderProductsRequest{" +
                "cartIds=" + cartIds +
                ", point=" + point +
                '}';
    }
}
