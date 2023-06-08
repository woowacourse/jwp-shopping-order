package cart.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class OrderItem {

    private Long id;
    private final OrderProduct product;
    private final Quantity quantity;

    public OrderItem(OrderProduct product, Quantity quantity) {
        this(null, product, quantity);
    }

    public OrderItem(Long id, OrderProduct product, Quantity quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    public Money calculatePrice() {
        return product.getPrice().multiply(quantity.getCount());
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
        if (orderItem.id == null && id == null) {
            return false;
        }
        return Objects.equals(id, orderItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }

    public OrderProduct getProduct() {
        return product;
    }

    public int getQuantityCount() {
        return quantity.getCount();
    }

    public BigDecimal getProductPrice() {
        return product.getPrice().getValue();
    }

    public String getProductName() {
        return product.getName();
    }

    public String getProductImage() {
        return product.getImageUrl();
    }
}
