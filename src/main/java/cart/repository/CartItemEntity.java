package cart.repository;

public class CartItemEntity {
    private final Long id;
    private final Long memberId;
    private final Long productId;
    private final Long quantity;
    
    public CartItemEntity(final Long id, final Long memberId, final Long productId, final Long quantity) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
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
    
    public Long getQuantity() {
        return quantity;
    }
    
    @Override
    public String toString() {
        return "CartItemEntity{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
