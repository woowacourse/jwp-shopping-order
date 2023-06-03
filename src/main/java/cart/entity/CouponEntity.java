package cart.entity;

import cart.domain.Money;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.discountPolicy.DeliveryPolicy;
import cart.domain.coupon.discountPolicy.DiscountPolicy;
import cart.domain.coupon.discountPolicy.PercentPolicy;
import cart.domain.coupon.discountPolicy.PolicyType;
import cart.domain.coupon.discountPolicy.PricePolicy;

import java.math.BigDecimal;

public class CouponEntity {

    private final Long id;
    private final String name;
    private final String policyType;
    private final BigDecimal discountValue;
    private final BigDecimal minimumPrice;

    public CouponEntity(
            final Long id,
            final String name,
            final String policyType,
            final BigDecimal value,
            final BigDecimal minimumPrice
    ) {
        this.id = id;
        this.name = name;
        this.policyType = policyType;
        this.discountValue = value;
        this.minimumPrice = minimumPrice;
    }

    public CouponEntity(
            final String name,
            final String policyType,
            final BigDecimal value,
            final BigDecimal minimumPrice
    ) {
        this(null, name, policyType, value, minimumPrice);
    }

    public static CouponEntity from(final Coupon coupon) {
        return new CouponEntity(
                null,
                coupon.getName(),
                coupon.getDiscountPolicy().getName(),
                coupon.getDiscountValue(),
                coupon.getMinimumPrice().getValue()
        );
    }

    public Coupon toDomain() {
        return new Coupon(
                id,
                name,
                getDiscountPolicy(),
                discountValue,
                new Money(minimumPrice)
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

    public BigDecimal getMinimumPrice() {
        return minimumPrice;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }
}
