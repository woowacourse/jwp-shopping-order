package cart.sale;

import cart.cart.Cart;
import cart.discountpolicy.DiscountPolicy;

public class Sale {
    private final String name;
    private final DiscountPolicy discountPolicy;

    public Sale(String name, DiscountPolicy discountPolicy) {
        this.name = name;
        this.discountPolicy = discountPolicy;
    }

    public void apply(Cart cart) {
        this.discountPolicy.discount(cart);
    }
}
