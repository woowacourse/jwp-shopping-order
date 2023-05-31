package cart.domain.coupon.policy;

import java.math.BigDecimal;

public class RateDiscountPolicy implements DiscountPolicy {

    private final DiscountPolicyType discountPolicyType;
    private final Long rate;

    public RateDiscountPolicy(final Long rate) {
        this.discountPolicyType = DiscountPolicyType.RATE;
        this.rate = rate;
    }

    @Override
    public BigDecimal calculatePrice(final Long price) {
        return BigDecimal.valueOf(price).subtract(BigDecimal.valueOf(price * (rate * 0.01)));
    }

    @Override
    public Long getDiscountPrice() {
        return rate;
    }

    @Override
    public DiscountPolicyType getPolicyType() {
        return discountPolicyType;
    }
}
