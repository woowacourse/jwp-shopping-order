package cart.domain;

import cart.exception.OrderException;

public class OrderItem {

    private static final int MINIMUM_QUANTITY = 1;

    private final Long id;
    private final Product product;
    private final int quantity;

    public OrderItem(Product product, int quantity) {
        this(null, product, quantity);
    }

    public OrderItem(Long id, Product product, int quantity) {
        validateQuantity(quantity);
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    private void validateQuantity(int quantity) {
        if (quantity < MINIMUM_QUANTITY) {
            throw new OrderException.InvalidQuantity(MINIMUM_QUANTITY);
        }
    }

    public Money calculateTotalPrice() {
        Money price = product.price();

        return price.multiply(quantity);
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
