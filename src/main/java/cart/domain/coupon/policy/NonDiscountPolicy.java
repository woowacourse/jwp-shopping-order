package cart.domain.coupon.policy;

import java.math.BigDecimal;

public class NonDiscountPolicy implements DiscountPolicy {
    @Override
    public BigDecimal calculatePrice(final Long price) {
        return BigDecimal.valueOf(price);
    }

    @Override
    public Long getDiscountPrice() {
        return 0L;
    }

    @Override
    public DiscountPolicyType getPolicyType() {
        return DiscountPolicyType.NONE;
    }
}
