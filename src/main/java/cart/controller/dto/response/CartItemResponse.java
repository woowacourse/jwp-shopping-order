package cart.controller.dto.response;

import cart.entity.CartItemEntity;

public class CartItemResponse {
    private Long id;
    private int quantity;
    private ProductResponse product;

    private CartItemResponse(Long id, int quantity, ProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public static CartItemResponse of(CartItemEntity cartItemEntity) {
        return new CartItemResponse(
                cartItemEntity.getId(),
                cartItemEntity.getQuantity(),
                ProductResponse.of(cartItemEntity.getProduct())
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
