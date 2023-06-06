package cart.entity;

public class OrderDetailEntity {

    private final Long id;
    private final Long ordersId;
    private final Long productId;
    private final String productName;
    private final int productPrice;
    private final String productImageUrl;
    private final int orderQuantity;

    public OrderDetailEntity(final Long id, final OrderDetailEntity other) {
        this(id, other.ordersId, other.productId, other.productName, other.productPrice, other.productImageUrl, other.orderQuantity);
    }

    private OrderDetailEntity(
            final Long id,
            final Long ordersId,
            final Long productId,
            final String productName,
            final int productPrice,
            final String productImageUrl,
            final int orderQuantity
    ) {
        this.id = id;
        this.ordersId = ordersId;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
        this.orderQuantity = orderQuantity;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public Long getOrdersId() {
        return ordersId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public static class Builder {

        private Long id;
        private Long ordersId;
        private Long productId;
        private String productName;
        private int productPrice;
        private String productImageUrl;
        private int orderQuantity;

        public Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public Builder ordersId(final Long ordersId) {
            this.ordersId = ordersId;
            return this;
        }

        public Builder productId(final Long productId) {
            this.productId = productId;
            return this;
        }

        public Builder productName(final String productName) {
            this.productName = productName;
            return this;
        }

        public Builder productPrice(final int productPrice) {
            this.productPrice = productPrice;
            return this;
        }

        public Builder productImageUrl(final String productImageUrl) {
            this.productImageUrl = productImageUrl;
            return this;
        }

        public Builder orderQuantity(final int orderQuantity) {
            this.orderQuantity = orderQuantity;
            return this;
        }

        public OrderDetailEntity build() {
            return new OrderDetailEntity(id, ordersId, productId, productName, productPrice, productImageUrl, orderQuantity);
        }
    }
}
