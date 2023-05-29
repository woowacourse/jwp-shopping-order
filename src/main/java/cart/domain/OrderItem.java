package cart.domain;

import java.util.Objects;

public class OrderItem {
    private Long id;
    private Product product;
    private Quantity quantity;
    private Price price;

    public OrderItem(Long id, Product product, Quantity quantity, Price price) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
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
}
