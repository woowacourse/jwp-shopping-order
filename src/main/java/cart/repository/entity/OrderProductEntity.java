package cart.repository.entity;

public class OrderProductEntity {

    private final Long id;
    private final long orderId;
    private final long productId;
    private final String productName;
    private final int productPrice;
    private final String productImageUrl;
    private final int quantity;

    private OrderProductEntity(Long id, long orderId, long productId, String productName, int productPrice, String productImageUrl, int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getProductId() {
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

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "OrderProductEntity{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", productImageUrl='" + productImageUrl + '\'' +
                ", quantity=" + quantity +
                '}';
    }

    public static class Builder {

        private long id;
        private long orderId;
        private long productId;
        private String productName;
        private int productPrice;
        private String productImageUrl;
        private int quantity;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder orderId(long orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder productId(long productId) {
            this.productId = productId;
            return this;
        }

        public Builder productName(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder productPrice(int productPrice) {
            this.productPrice = productPrice;
            return this;
        }

        public Builder productImageUrl(String productImageUrl) {
            this.productImageUrl = productImageUrl;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderProductEntity build() {
            return new OrderProductEntity(id, orderId, productId, productName, productPrice, productImageUrl, quantity);
        }
    }
}
