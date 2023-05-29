package cart.cartitem.presentation.dto;

import cart.cartitem.domain.CartItem;
import cart.product.presentation.dto.ProductResponse;

public class CartItemResponse {

    private Long id;
    private int quantity;
    private ProductResponse product;

    public CartItemResponse(Long id, int quantity, ProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public static CartItemResponse from(CartItem cartItem) {
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getQuantity(),
                new ProductResponse(
                        cartItem.getProductId(),
                        cartItem.getName(),
                        cartItem.getProductPrice(),
                        cartItem.getImageUrl()
                )
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
