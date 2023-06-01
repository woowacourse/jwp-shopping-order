package cart.dao.entity;

public class CartItemEntity {

    private final Long id;
    private final long memberId;
    private final long productId;
    private final int quantity;

    public CartItemEntity(final long memberId, final long productId, final int quantity) {
        this(null, memberId, productId, quantity);
    }

    public CartItemEntity(final Long id, final long memberId, final long productId, final int quantity) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public long getMemberId() {
        return memberId;
    }

    public long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
