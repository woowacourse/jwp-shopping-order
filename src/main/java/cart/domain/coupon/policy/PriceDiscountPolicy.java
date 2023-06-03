package cart.domain.coupon.policy;

import java.math.BigDecimal;

public class PriceDiscountPolicy implements DiscountPolicy {

    private final DiscountPolicyType discountPolicyType;
    private final Long price;

    public PriceDiscountPolicy(final Long price) {
        this.discountPolicyType = DiscountPolicyType.PRICE;
        this.price = price;
    }

    @Override
    public BigDecimal calculatePrice(final Long price) {
        return BigDecimal.valueOf(price - this.price);
    }

    @Override
    public Long getDiscountValue() {
        return price;
    }

    @Override
    public DiscountPolicyType getPolicyType() {
        return discountPolicyType;
    }
}
