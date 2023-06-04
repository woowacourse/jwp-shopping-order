package cart.domain;

public class OrderItem {
    private final Long id;
    private final Product product;
    private final int quantity;

    public OrderItem(Long id, Product product, int quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    public OrderItem(Product product, int quantity) {
        this(null, product, quantity);
    }

    public int calculatePrice() {
        return quantity * product.getPrice();
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
