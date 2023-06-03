package cart.domain.cartitem;

import cart.domain.product.ProductWithId;
import java.util.Objects;

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

    public boolean isSameProductId(final Long productId) {
        return Objects.equals(product.getProductId(), productId);
    }

    public boolean isDeleted() {
        return product.isDeleted();
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
