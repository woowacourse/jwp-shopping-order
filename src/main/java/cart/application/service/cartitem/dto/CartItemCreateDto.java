package cart.application.service.cartitem.dto;

import cart.ui.cartitem.dto.CartItemRequest;

public class CartItemCreateDto {

    private final Long productId;

    public CartItemCreateDto(Long productId) {
        this.productId = productId;
    }

    public static CartItemCreateDto from(final CartItemRequest cartItemRequest) {
        return new CartItemCreateDto(cartItemRequest.getProductId());
    }

    public Long getProductId() {
        return productId;
    }

}
