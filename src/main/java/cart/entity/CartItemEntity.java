package cart.entity;

public class CartItemEntity {

    private final Long id;
    private final int quantity;
    private final Long memberId;
    private final Long productId;

    public CartItemEntity(final Long id, final int quantity, final Long memberId, final Long productId) {
        this.id = id;
        this.quantity = quantity;
        this.memberId = memberId;
        this.productId = productId;
    }

    public CartItemEntity(final int quantity, final Long memberId, final Long productId) {
        this(null, quantity, memberId, productId);
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
    }
}
