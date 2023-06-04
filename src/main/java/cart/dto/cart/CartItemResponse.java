package cart.dto.cart;

import cart.domain.cart.CartItem;
import cart.domain.product.Item;
import cart.dto.product.ProductResponse;

public class CartItemResponse {
    private Long id;
    private int quantity;
    private ProductResponse product;

    private CartItemResponse(Long id, int quantity, ProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public static CartItemResponse of(final Item item) {
        return new CartItemResponse(
                item.getId(),
                item.getQuantity(),
                ProductResponse.of(item.getProduct())
        );
    }

    public static CartItemResponse of(CartItem cartItem) {
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getItem().getQuantity(),
                ProductResponse.of(cartItem.getItem().getProduct())
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
