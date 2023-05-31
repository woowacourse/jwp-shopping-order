package shop.persistence.entity;

public class CartEntity {
    private final Long id;
    private final Long memberId;
    private final Long productId;
    private final int quantity;

    public CartEntity(Long id, Long memberId, Long productId, int quantity) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public CartEntity(Long memberId, Long productId, int quantity) {
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

    public int getQuantity() {
        return quantity;
    }
}
