package cart.discountpolicy;

import cart.cart.Cart;
import cart.product.Product;

public interface DiscountPolicy {
    void discount(Cart cart);

    void discount(Product product);
}

