package cart.entity;

public class CartItemEntity {
    private final Long id;
    private final Long memberId;
    private final Long productId;
    private final Integer quantity;

    public CartItemEntity(final Long id, final Long memberId, final Long productId, final Integer quantity) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public boolean equalsProductId(Long productId) {
        return this.productId.equals(productId);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
