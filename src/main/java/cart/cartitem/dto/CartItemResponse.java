package cart.cartitem.dto;

import cart.cartitem.domain.CartItem;
import cart.product.dto.ProductResponse;

import java.util.Objects;

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
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CartItemResponse that = (CartItemResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(quantity, that.quantity) && Objects.equals(product, that.product);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, quantity, product);
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
