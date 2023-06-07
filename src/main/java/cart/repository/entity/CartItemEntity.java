package cart.repository.entity;

import java.util.Objects;

public class CartItemEntity {

    private final Long id;
    private final long memberId;
    private final long productId;
    private final int quantity;

    public CartItemEntity(Long id, long memberId, long productId, int quantity) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public long getMemberId() {
        return memberId;
    }

    public long getProductId() {
        return productId;
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

    public static class Builder {

        private Long id;
        private long memberId;
        private long productId;
        private int quantity;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Builder builder = (Builder) o;
            return Objects.equals(id, builder.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder memberId(long memberId) {
            this.memberId = memberId;
            return this;
        }

        public Builder productId(long productId) {
            this.productId = productId;
            return this;
        }


        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public CartItemEntity build() {
            return new CartItemEntity(id, memberId, productId, quantity);
        }
    }
}
