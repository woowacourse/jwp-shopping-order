package cart.domain.cartitem;

import cart.domain.product.dto.ProductWithId;

public class CartItemWithId {

    private final Long id;
    private int quantity;
    private final ProductWithId product;

    public CartItemWithId(final Long id, final int quantity, final ProductWithId product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public void changeQuantity(final int quantity) {
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductWithId getProduct() {
        return product;
    }
}
