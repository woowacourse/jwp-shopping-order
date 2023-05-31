package cart.domain.order;

import cart.domain.Product;

public class OrderProduct {
    private final Product product;
    private final int quantity;

    public OrderProduct(final Product product, final int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public int price() {
        return product.getPrice() * quantity;
    }
}
