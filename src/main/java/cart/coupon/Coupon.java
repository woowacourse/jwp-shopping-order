package cart.coupon;

import cart.cart.Cart;
import cart.discountpolicy.DiscountPolicy;

public class Coupon {
    private final Long id;
    private final String name;
    private final DiscountPolicy discountPolicy;

    public Coupon(Long id, String name, DiscountPolicy discountPolicy) {
        this.id = id;
        this.name = name;
        this.discountPolicy = discountPolicy;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void apply(Cart cart) {
        this.discountPolicy.discount(cart);
    }
}
