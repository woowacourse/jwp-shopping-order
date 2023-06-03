package cart.domain.cartitem.dto;

import cart.domain.product.dto.ProductWithId;

public class CartItemWithId {

    private final Long cartId;
    private final int quantity;
    private final ProductWithId product;

    public CartItemWithId(final int quantity, final ProductWithId product) {
        this(null, quantity, product);
    }

    public CartItemWithId(final Long cartId, final int quantity, final ProductWithId product) {
        this.cartId = cartId;
        this.quantity = quantity;
        this.product = product;
    }

    public Long getCartId() {
        return cartId;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductWithId getProduct() {
        return product;
    }
}
