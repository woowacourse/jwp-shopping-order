package cart.domain;

public class OrderItem {

    private Long id;
    private String name;
    private int price;
    private String imageUrl;
    private int quantity;
    private int totalPayment;

    private OrderItem(Long id, String name, int price, String imageUrl, int quantity, int totalPayment) {
        this.id = id;
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
        private String name;
        private int price;
        private String imageUrl;
        private int quantity;
        private int totalPayment;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder price(int price) {
            this.price = price;
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
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
            return new OrderItem(id, name, price, imageUrl, quantity, totalPayment);
        }
    }
}
