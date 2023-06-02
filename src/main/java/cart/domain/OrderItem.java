package cart.domain;

public class OrderItem {

    private final Long id;
    private final long orderId;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final int quantity;
    private final int totalPayment;

    private OrderItem(Long id, long orderId, String name, int price, String imageUrl, int quantity, int totalPayment) {
        this.id = id;
        this.orderId = orderId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.totalPayment = totalPayment;
    }

    public Long getId() {
        return id;
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

    public int getTotalPayment() {
        return totalPayment;
    }

    public static class Builder {

        private Long id;
        private long orderId;
        private String productName;
        private int productPrice;
        private String productImageUrl;
        private int quantity;
        private int totalPayment;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder orderId(long orderId) {
            this.orderId = orderId;
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

        public Builder totalPayment(int totalPayment) {
            this.totalPayment = totalPayment;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(id, orderId, productName, productPrice, productImageUrl, quantity, totalPayment);
        }
    }
}
