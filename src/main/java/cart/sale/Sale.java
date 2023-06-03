package cart.sale;

import cart.cart.Cart;
import cart.discountpolicy.DiscountPolicy;
import cart.product.Product;

public class Sale {
    private final Long id;
    private final String name;
    private final DiscountPolicy discountPolicy;

    public Sale(Long id, String name, DiscountPolicy discountPolicy) {
        this.id = id;
        this.name = name;
        this.discountPolicy = discountPolicy;
    }

    public void apply(Cart cart) {
        this.discountPolicy.discount(cart);
    }

    public void apply(Product product) {
        this.discountPolicy.discount(product);
    }
}
