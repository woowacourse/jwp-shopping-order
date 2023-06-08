package cart.domain;

import java.util.Objects;

public class OrderItem {

    private Long id;
    private final Product product;
    private final Integer quantity;

    public OrderItem(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public OrderItem(Long id, Product product, Integer quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id) && Objects.equals(product, orderItem.product) && Objects.equals(quantity, orderItem.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product, quantity);
    }
}
