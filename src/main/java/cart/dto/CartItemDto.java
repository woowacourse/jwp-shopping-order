package cart.dto;

import cart.domain.CartItem;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(implementation = CartItemDto.class)
public class CartItemDto {

    private Long id;
    private int quantity;
    private ProductResponse product;

    private CartItemDto(Long id, int quantity, ProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public static CartItemDto of(CartItem cartItem) {
        return new CartItemDto(
                cartItem.getId(),
                cartItem.getQuantity(),
                ProductResponse.of(cartItem.getProduct())
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
