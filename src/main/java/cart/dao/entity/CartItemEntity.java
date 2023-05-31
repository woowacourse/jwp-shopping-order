package cart.dao.entity;

import cart.domain.CartItem;
import cart.domain.Member;

import java.util.Map;
import java.util.Objects;

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

    public CartItem toCartItem(final Map<Long, ProductEntity> allProductsById, final Member member) {
        if (!Objects.equals(memberId, member.getId())) {
            throw new UnsupportedOperationException("장바구니 정보와 회원 정보가 일치하지 않습니다.");
        }
        return new CartItem(
                id,
                quantity,
                allProductsById.get(productId).toProduct(),
                member
        );
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CartItemEntity that = (CartItemEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(memberId, that.memberId) && Objects.equals(productId, that.productId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, memberId, productId, quantity);
    }
}
