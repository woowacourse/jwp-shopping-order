package cart.domain;

import java.util.Objects;

public class OrderItem {
    private final Long id;
    private final Product product;
    private final Integer quantity;

    public OrderItem(final Long id, final Product product, final Integer quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    public Integer calculatePrice() {
        return product.getPrice() * quantity;
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
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id)
                && Objects.equals(product, orderItem.product)
                && Objects.equals(quantity, orderItem.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product, quantity);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}
