package cart.domain;

import cart.domain.policy.DiscountPolicy;

public class Coupon {
    private final Long id;
    private final String name;
    private final DiscountPolicy discountPolicy;

    public Coupon(Long id, String name, DiscountPolicy discountPolicy) {
        this.id = id;
        this.name = name;
        this.discountPolicy = discountPolicy;
    }

    public Coupon(String name, DiscountPolicy discountPolicy) {
        this(null, name, discountPolicy);
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

}
