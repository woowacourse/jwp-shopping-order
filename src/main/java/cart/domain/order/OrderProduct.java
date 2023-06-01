package cart.domain.order;

import cart.domain.CartItem;
import cart.domain.Product;

public class OrderProduct {
    private final Product product;
    private final int quantity;

    public OrderProduct(final Product product, final int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public static OrderProduct of(final CartItem cartItem) {
        return new OrderProduct(cartItem.getProduct(), cartItem.getQuantity());
    }

    public int price() {
        return product.getPrice() * quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
