package cart.domain.cartitem;

import cart.domain.product.Product;
import java.util.Objects;

public class CartItem {

    private final Long cartId;
    private final int quantity;
    private final Product product;

    public CartItem(final int quantity, final Product product) {
        this(null, quantity, product);
    }

    public CartItem(final Long cartId, final int quantity, final Product product) {
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

    public Product getProduct() {
        return product;
    }
}
