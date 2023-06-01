package cart.dao.entity;

import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;

import java.util.Objects;

public class CartItemEntity {

    private final Long id;
    private final Long productId;
    private final Long memberId;
    private final int quantity;

    public CartItemEntity(Long productId, Long memberId, int quantity) {
        this(null, productId, memberId, quantity);
    }

    public CartItemEntity(Long id, Long productId, Long memberId, int quantity) {
        this.id = id;
        this.productId = productId;
        this.memberId = memberId;
        this.quantity = quantity;
    }

    public CartItem toDomain(Product product, Member member) {
        return new CartItem(id, product, member, quantity);
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItemEntity that = (CartItemEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CartItemEntity{" +
                "id=" + id +
                ", productId=" + productId +
                ", memberId=" + memberId +
                ", quantity=" + quantity +
                '}';
    }
}
