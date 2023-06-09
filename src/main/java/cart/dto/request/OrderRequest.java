package cart.dto.request;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class OrderRequest {

    @NotNull(message = "배송료를 입력해주세요")
    private Integer deliveryFee;

    @Size(message = "주문에 상품이 존재하지 않습니다.", min = 1)
    private List<OrderItemRequest> orderItems;

    public OrderRequest() {
    }

    public OrderRequest(Integer deliveryFee, List<OrderItemRequest> orderItems) {
        this.deliveryFee = deliveryFee;
        this.orderItems = orderItems;
    }

    public List<OrderItemRequest> getOrderItems() {
        return orderItems;
    }

    public Integer getDeliveryFee() {
        return deliveryFee;
    }
}
