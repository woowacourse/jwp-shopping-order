package cart.domain.Order;

import cart.domain.Product.Price;
import cart.domain.Product.Product;

public class OrderItem {
    Product product;
    Quantity quantity;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = new Quantity(quantity);
    }

    public Product getProduct() {
        return product;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Price totalPrice() {
        return product.multiplyPriceBy(quantity.quantity());
    }

    public Price getPrice() {
        return product.getPrice();
    }
}
