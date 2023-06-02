package cart.dto;

import cart.domain.CartItem;

public class CartItemDto {
    private Long cartItemId;
    private int quantity;
    private ProductDto product;

    public CartItemDto(Long cartItemId, int quantity, ProductDto product) {
        this.cartItemId = cartItemId;
        this.quantity = quantity;
        this.product = product;
    }

    public static CartItemDto from(CartItem cartItem) {
        return new CartItemDto(
                cartItem.getId(),
                cartItem.getQuantity(),
                ProductDto.from(cartItem.getProduct())
        );
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductDto getProduct() {
        return product;
    }
}
