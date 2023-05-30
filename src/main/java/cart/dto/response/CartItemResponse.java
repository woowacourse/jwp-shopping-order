package cart.dto.response;

import cart.domain.CartItem;

public class CartItemResponse {
    private Long id;
    private long quantity;
    private ProductResponse product;

    private CartItemResponse(Long id, long quantity, ProductResponse product) {
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

    public long getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }
}
