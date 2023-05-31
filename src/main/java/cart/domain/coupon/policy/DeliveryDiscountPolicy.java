package cart.domain.coupon.policy;

import java.math.BigDecimal;

public class DeliveryDiscountPolicy implements DiscountPolicy {

    private final DiscountPolicyType discountPolicyType;
    private static final Long DELIVERY_FEE = 3000L;

    public DeliveryDiscountPolicy() {
        this.discountPolicyType = DiscountPolicyType.DELIVERY;
    }

    @Override
    public BigDecimal calculatePrice(final Long price) {
        return BigDecimal.valueOf(price - DELIVERY_FEE);
    }

    @Override
    public Long getDiscountPrice() {
        return 0L;
    }

    @Override
    public DiscountPolicyType getPolicyType() {
        return discountPolicyType;
    }
}
