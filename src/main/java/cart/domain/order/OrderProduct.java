package cart.domain.order;

public class OrderProduct {

    private Long id;
    private Long orderId;
    private final long productId;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final int quantity;
    private final int totalPrice;

    private OrderProduct(Long id, long orderId, long productId, String name, int price, String imageUrl, int quantity, int totalPrice) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
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

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    @Override
    public String toString() {
        return "OrderProduct{" +
                "id=" + id +
                ", productId=" + productId +
                ", orderId=" + orderId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                '}';
    }

    public static class Builder {

        private Long id;
        private long orderId;
        private long productId;
        private String productName;
        private int productPrice;
        private String productImageUrl;
        private int quantity;
        private int totalPrice;

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

        public Builder productName(String name) {
            this.productName = name;
            return this;
        }

        public Builder productPrice(int price) {
            this.productPrice = price;
            return this;
        }

        public Builder productImageUrl(String imageUrl) {
            this.productImageUrl = imageUrl;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder totalPrice(int totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public OrderProduct build() {
            return new OrderProduct(id, productId, orderId, productName, productPrice, productImageUrl, quantity, totalPrice);
        }
    }
}
