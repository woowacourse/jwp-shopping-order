package cart.domain;

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
}
