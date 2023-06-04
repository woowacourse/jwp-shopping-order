package cart.domain.order;

import cart.domain.Product;


public class OrderItem {

    private final Long id;
    private final Product product;
    private final Long quantity;

    public OrderItem(Product product, Long quantity) {
        this(null, product, quantity);
    }

    public OrderItem(Long id, Product product, Long quantity) {
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

    public Long getQuantity() {
        return quantity;
    }
}
