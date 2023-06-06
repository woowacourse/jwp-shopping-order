package cart.repository.entity;

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

    public static class Builder {

        private Long id;
        private long memberId;
        private long productId;
        private int quantity;

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
