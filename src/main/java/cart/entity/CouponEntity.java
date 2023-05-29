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
    private final long discountPrice;
    private final int discountPercent;
    private final boolean discountDeliveryFee;
    private final String conditionType;
    private final long minimumPrice;

    public CouponEntity(
            final Long id,
            final String name,
            final String policyType,
            final long discountPrice,
            final int discountPercent,
            final boolean discountDeliveryFee,
            final String conditionType,
            final long minimumPrice
    ) {
        this.id = id;
        this.name = name;
        this.policyType = policyType;
        this.discountPrice = discountPrice;
        this.discountPercent = discountPercent;
        this.discountDeliveryFee = discountDeliveryFee;
        this.conditionType = conditionType;
        this.minimumPrice = minimumPrice;
    }

    public Coupon toDomain() {
        return new Coupon(
                id,
                name,
                getDiscountPolicy(),
                getDiscountCondition()
        );
    }

    private DiscountCondition getDiscountCondition() {
        final ConditionType type = ConditionType.from(conditionType);
        if (type == ConditionType.MINIMUM_PRICE) {
            return new TotalItemsPriceCondition(minimumPrice);
        }
        return new NoneCondition();
    }

    private DiscountPolicy getDiscountPolicy() {
        final PolicyType type = PolicyType.from(policyType);
        if (type == PolicyType.PRICE) {
            return new PricePolicy(discountPrice);
        }
        if (type == PolicyType.PERCENT) {
            return new PercentPolicy(discountPercent);
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

    public long getDiscountPrice() {
        return discountPrice;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public boolean getDiscountDeliveryFee() {
        return discountDeliveryFee;
    }

    public String getConditionType() {
        return conditionType;
    }

    public long getMinimumPrice() {
        return minimumPrice;
    }
}
