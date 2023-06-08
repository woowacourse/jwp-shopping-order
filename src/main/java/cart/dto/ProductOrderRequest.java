package cart.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ProductOrderRequest {

    @NotNull
    @Positive
    private Long productId;

    @NotNull
    @Positive
    private Integer quantity;

    public ProductOrderRequest() {
    }

    public ProductOrderRequest(Long productId, Integer quantity) {
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
