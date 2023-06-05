package cart.domain.order;

import cart.domain.price.Price;
import cart.domain.product.Product;
import cart.domain.product.Quantity;
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
}
