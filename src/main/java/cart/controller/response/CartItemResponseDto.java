package cart.controller.response;

import cart.domain.CartItem;

public class CartItemResponseDto {

    private Long id;
    private int quantity;
    private ProductResponseDto product;

    private CartItemResponseDto() {
    }

    private CartItemResponseDto(Long id, int quantity, ProductResponseDto product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public static CartItemResponseDto of(CartItem cartItem) {
        return new CartItemResponseDto(
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
