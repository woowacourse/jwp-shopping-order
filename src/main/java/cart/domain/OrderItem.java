package cart.domain;

import java.util.Objects;

public class OrderItem {

    private final Long id;
    private final Product orderedProduct;
    private final int quantity;

    public OrderItem(CartItem cartItem) {
        this(
                null,
                cartItem.getProduct(),
                cartItem.getQuantity()
        );
    }

    public OrderItem(Long id, Product orderedProduct, int quantity) {
        this.id = id;
        this.orderedProduct = orderedProduct;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Product getOrderedProduct() {
        return orderedProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public Money getTotal() {
        return orderedProduct.getPrice().multiply(quantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        OrderItem orderItem = (OrderItem)o;

        return Objects.equals(id, orderItem.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", orderedProduct=" + orderedProduct +
                ", quantity=" + quantity +
                '}';
    }
}
