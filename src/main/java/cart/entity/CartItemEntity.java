package cart.entity;

public class CartItemEntity {
    private final Long id;
    private final Long memberId;
    private final Long productId;
    private final Integer quantity;

    public CartItemEntity(Long id, Long memberId, Long productId, Integer quantity) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public CartItemEntity(Long memberId, Long productId, Integer quantity) {
        this(null, memberId, productId, quantity);
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
