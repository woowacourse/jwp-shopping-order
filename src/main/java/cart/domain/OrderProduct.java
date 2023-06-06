package cart.domain;

import java.util.Objects;

public class OrderProduct {
    private final Long id;
    private final Order order;
    private final Product product;
    private final int quantity;

    public OrderProduct(final Order order, final Product product, final int quantity) {
        this(null, order, product, quantity);
    }

    public OrderProduct(final Long id, final Order order, final Product product, final int quantity) {
        this.id = id;
        this.order = order;
        this.product = product;
        this.quantity = quantity;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderProduct that = (OrderProduct) o;
        return getQuantity() == that.getQuantity() && Objects.equals(getId(), that.getId())
                && Objects.equals(getOrder(), that.getOrder()) && Objects.equals(getProduct(),
                that.getProduct());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOrder(), getProduct(), getQuantity());
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
