package cart.cartitem.dto;

import cart.cartitem.domain.CartItem;
import cart.product.dto.ProductResponse;

public class CartItemResponse {
    private Long id;
    private Long quantity;
    private ProductResponse product;

    private CartItemResponse(Long id, Long quantity, ProductResponse product) {
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

    public Long getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }
    
    @Override
    public String toString() {
        return "CartItemResponse{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", product=" + product +
                '}';
    }
}
