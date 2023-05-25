package cart.order.domain;

import static cart.order.exception.OrderExceptionType.INVALID_QUANTITY;

import cart.order.exception.OrderException;
import cart.product.domain.Product;

public class OrderItem {

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
        if (quantity < 1) {
            throw new OrderException(INVALID_QUANTITY);
        }
    }

    public int price() {
        return product.getPrice() * quantity;
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
