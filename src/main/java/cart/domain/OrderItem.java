package cart.domain;

import java.math.BigDecimal;

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
