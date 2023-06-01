package cart.domain.coupon;

import cart.domain.Price;
import cart.domain.coupon.Coupon;
import cart.domain.policy.DiscountPolicy;

public class ProductCoupon implements Coupon {
    public static final String RANGE = "single";
    private final Long id;
    private final String name;
    private final DiscountPolicy discountPolicy;


    public ProductCoupon(Long id, String name, DiscountPolicy discountPolicy) {
        this.id = id;
        this.name = name;
        this.discountPolicy = discountPolicy;
    }

    public ProductCoupon(String name, DiscountPolicy discountPolicy) {
        this(null, name, discountPolicy);
    }

    public Price apply(Price price) {
        return discountPolicy.discount(price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDiscountPolicyName() {
        return discountPolicy.getName();
    }

    public int getDiscountValue() {
        return discountPolicy.getValue();
    }

    @Override
    public String getRange() {
        return RANGE;
    }
}
