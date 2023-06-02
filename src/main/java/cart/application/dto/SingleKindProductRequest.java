package cart.application.dto;

import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SingleKindProductRequest {

    @Positive(message = "유효하지 않은 상품 id 입니다")
    private Long productId;

    @Positive(message = "구매할 상품의 개수는 1개 이상이어야 합니다")
    @JsonProperty("Quantity")
    private Integer quantity;

    private SingleKindProductRequest() {
    }

    public SingleKindProductRequest(Long productId, Integer quantity) {
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
