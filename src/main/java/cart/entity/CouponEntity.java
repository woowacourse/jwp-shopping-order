package cart.entity;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.discountCondition.ConditionType;
import cart.domain.coupon.discountCondition.DiscountCondition;
import cart.domain.coupon.discountCondition.NoneCondition;
import cart.domain.coupon.discountCondition.TotalItemsPriceCondition;
import cart.domain.coupon.discountPolicy.DeliveryPolicy;
import cart.domain.coupon.discountPolicy.DiscountPolicy;
import cart.domain.coupon.discountPolicy.PercentPolicy;
import cart.domain.coupon.discountPolicy.PolicyType;
import cart.domain.coupon.discountPolicy.PricePolicy;

public class CouponEntity {

    private final Long id;
    private final String name;
    private final String policyType;
    private final long value;
    private final long minimumPrice;

    public CouponEntity(
            final Long id,
            final String name,
            final String policyType,
            final long value,
            final long minimumPrice
    ) {
        this.id = id;
        this.name = name;
        this.policyType = policyType;
        this.value = value;
        this.minimumPrice = minimumPrice;
    }

    public Coupon toDomain() {
        return new Coupon(
                id,
                name,
                getDiscountPolicy(),
                value,
                minimumPrice
        );
    }

    private DiscountPolicy getDiscountPolicy() {
        final PolicyType type = PolicyType.from(policyType);
        if (type == PolicyType.PRICE) {
            return new PricePolicy();
        }
        if (type == PolicyType.PERCENT) {
            return new PercentPolicy();
        }
        return new DeliveryPolicy();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPolicyType() {
        return policyType;
    }

    public long getMinimumPrice() {
        return minimumPrice;
    }
}
