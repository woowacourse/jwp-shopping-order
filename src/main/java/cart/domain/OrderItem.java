package cart.domain;

import java.math.BigDecimal;

public class OrderItem {

    private Long id;
    private final Product product;
    private final Quantity quantity;

    public OrderItem(Product product, Quantity quantity) {
        this(null, product, quantity);
    }

    public OrderItem(Long id, Product product, Quantity quantity) {
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

    public Product getProduct() {
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
