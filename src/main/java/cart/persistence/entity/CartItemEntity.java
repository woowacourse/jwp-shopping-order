package cart.persistence.entity;

public class CartItemEntity {
    private final Long id;
    private final Integer quantity;
    private final Long productId;
    private final Long memberId;

    public CartItemEntity(Long id, Integer quantity, Long productId, Long memberId) {
        this.id = id;
        this.quantity = quantity;
        this.productId = productId;
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
