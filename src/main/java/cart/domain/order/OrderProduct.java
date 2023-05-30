package cart.domain.order;

import cart.domain.cart.Quantity;
import cart.domain.product.Price;
import cart.domain.product.Product;

public class OrderProduct {

    private final Product product;
    private final Price purchasedPrice;
    private final Quantity quantity;

    public OrderProduct(final Product product, final int purchasedPrice, final int quantity) {
        this.product = product;
        this.purchasedPrice = new Price(purchasedPrice);
        this.quantity = new Quantity(quantity);
    }

    public Product getProduct() {
        return product;
    }

    public int getPurchasedPrice() {
        return purchasedPrice.getValue();
    }

    public int getQuantity() {
        return quantity.getValue();
    }

    public int getAmount() {
        return purchasedPrice.getValue() * quantity.getValue();
    }
}
