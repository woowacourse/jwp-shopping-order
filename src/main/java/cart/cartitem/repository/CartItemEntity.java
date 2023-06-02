package cart.cartitem.repository;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItemEntity that = (CartItemEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(memberId, that.memberId) && Objects.equals(productId, that.productId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, memberId, productId, quantity);
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
