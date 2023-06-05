package cart.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class OrderItemRequest {
    @NotNull(message = "상품 ID는 반드시 포함되어야 합니다.")
    @Positive(message = "상품 ID는 0 또는 음수가 될 수 없습니다.")
    private Long productId;

    @NotNull(message = "수량은 반드시 포함되어야 합니다.")
    @Positive(message = "수량은 0 또는 음수가 될 수 없습니다.")
    private Integer quantity;

    private OrderItemRequest() {
    }

    public OrderItemRequest(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
