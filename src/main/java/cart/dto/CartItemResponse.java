package cart.dto;

import cart.domain.CartItem;

public class CartItemResponse {

    private Long id;
    private int quantity;
    private ProductResponseDto product;

    private CartItemResponse() {
    }

    private CartItemResponse(Long id, int quantity, ProductResponseDto product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public static CartItemResponse of(CartItem cartItem) {
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getQuantity(),
                ProductResponseDto.from(cartItem.getProduct())
        );
    }

    public Long getId() {
        return id;
    }

    public ProductResponseDto getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
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
