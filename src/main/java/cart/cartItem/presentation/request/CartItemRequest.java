package cart.cartItem.presentation.request;

import cart.cartItem.application.dto.CartItemAddDto;

public class CartItemRequest {
    private Long productId;

    public CartItemRequest() {
    }

    public CartItemRequest(Long productId) {
        this.productId = productId;
    }

    public CartItemAddDto toDto() {
        return new CartItemAddDto(productId);
    }
    public Long getProductId() {
        return productId;
    }
}
