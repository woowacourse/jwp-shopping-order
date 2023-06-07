package cart.domain;

import java.util.Objects;

public class CartItemEntity {

    private final Long id;
    private final Long memberId;
    private final Long productId;
    private final int quantity;

    public CartItemEntity(final Long id, final Long memberId, final Long productId, final int quantity) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public CartItemEntity(final Long memberId, final Long productId, final int quantity) {
        this(null, memberId, productId, quantity);
    }

    public CartItemEntity(final Member member, final Product product, final Quantity quantity) {
        this(null, member.getId(), product.getId(), quantity.getValue());
    }

    public CartItemEntity(final Long id, final Member member, final Product product, final Quantity quantity) {
        this(id, member.getId(), product.getId(), quantity.getValue());
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CartItemEntity that = (CartItemEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
