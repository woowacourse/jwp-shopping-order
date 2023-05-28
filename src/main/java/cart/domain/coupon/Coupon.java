package cart.domain.coupon;

import cart.domain.discount.Policy;
import cart.domain.discount.PolicyDiscount;
import cart.domain.discount.PolicyPercentage;

public class Coupon {

    private final Long id;
    private final String name;
    private final Policy policy;

    public Coupon(final Long id, final String name, final Policy policy) {
        this.id = id;
        this.name = name;
        this.policy = policy;
    }

    public static Coupon from(final String name, final boolean isPercentage, final int value) {
        if (isPercentage) {
            return new Coupon(null, name, new PolicyPercentage(value));
        }

        return new Coupon(null, name, new PolicyDiscount(value));
    }

    public int calculate(final int total) {
        return policy.calculate(total);
    }

    public boolean isDeliveryCoupon() {
        return name.startsWith("DELIVERY");
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Policy getPolicy() {
        return policy;
    }
}
