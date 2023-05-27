package cart.entity;

public class CartItemEntity {

    private final Long id;
    private final Long cartId;
    private final Long productId;
    private final int quantity;

    public CartItemEntity(final Long id, final Long cartId, final Long productId, final int quantity) {
        this.id = id;
        this.cartId = cartId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getCartId() {
        return cartId;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
