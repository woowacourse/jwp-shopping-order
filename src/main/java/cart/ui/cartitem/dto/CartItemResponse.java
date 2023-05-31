package cart.ui.cartitem.dto;

import cart.application.service.cartitem.dto.CartItemResultDto;
import cart.ui.product.dto.ProductResponse;

public class CartItemResponse {
    private Long id;
    private int quantity;
    private ProductResponse product;

    private CartItemResponse(Long id, int quantity, ProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public static CartItemResponse of(CartItemResultDto cartItemResultDto) {
        return new CartItemResponse(
                cartItemResultDto.getId(),
                cartItemResultDto.getQuantity(),
                ProductResponse.of(cartItemResultDto.getProductResultDto())
        );
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }
}
