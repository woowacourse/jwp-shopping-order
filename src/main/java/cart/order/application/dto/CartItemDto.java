package cart.order.application.dto;

import cart.cartItem.application.dto.ProductInCartItemDto;
import cart.cartItem.domain.CartItem;

public class CartItemDto {
    private Long cartItemId;
    private int quantity;
    private ProductInCartItemDto product;

    public CartItemDto(Long cartItemId, int quantity, ProductInCartItemDto product) {
        this.cartItemId = cartItemId;
        this.quantity = quantity;
        this.product = product;
    }

    public static CartItemDto from(CartItem cartItem) {
        return new CartItemDto(
                cartItem.getId(),
                cartItem.getQuantity(),
                ProductInCartItemDto.from(cartItem.getProduct())
        );
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductInCartItemDto getProduct() {
        return product;
    }
}
