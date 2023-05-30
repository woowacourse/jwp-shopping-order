package cart.domain.order;

import cart.domain.cart.Quantity;
import cart.domain.product.Product;

public class OrderProduct {

    private final Product product;
    private final Integer purchasedPrice;
    private final Quantity quantity;

    public OrderProduct(final Product product, final Integer purchasedPrice, final int quantity) {
        this.product = product;
        this.purchasedPrice = purchasedPrice;
        this.quantity = new Quantity(quantity);
    }

    public Product getProduct() {
        return product;
    }

    public Integer getPurchasedPrice() {
        return purchasedPrice;
    }

    public int getQuantity() {
        return quantity.getValue();
    }
}
