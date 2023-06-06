package cart.domain;

import java.util.Objects;

public class OrderItem {
    private final Long id;
    private final Product product;
    private final Quantity quantity;
    private final Price price;

    public OrderItem(Long id, Product product, int quantity, long price) {
        this.id = id;
        this.product = product;
        this.quantity = new Quantity(quantity);
        this.price = Price.from(price);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(getId(), orderItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Price getPrice() {
        return price;
    }


    public static final class Builder {
        private Long id;
        private Product product;
        private int quantity;
        private long price;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder product(Product product) {
            this.product = product;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder price(long price) {
            this.price = price;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(id, product, quantity, price);
        }
    }
}
