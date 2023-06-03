package cart.dto;

import cart.domain.CartItem;

public class CartItemResponse {
    private final Long id;
    private final Integer quantity;
    private final ProductResponse product;

    private CartItemResponse(Long id, Integer quantity, ProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public static CartItemResponse of(CartItem cartItem) {
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getQuantity(),
                ProductResponse.of(cartItem.getProduct())
        );
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }
}
