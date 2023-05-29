package cart.dto;

import javax.validation.constraints.Positive;

public class CartItemRequest {
    private Long productId;

    @Positive(message = "수량은 양수여야합니다.")
    private int quantity;

    private CartItemRequest() {
    }

    public CartItemRequest(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
