package cart.entity;

import cart.domain.common.Money;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.DiscountPolicyType;

public class CouponEntity {

    private final Long id;
    private final String name;
    private final String policyType;
    private final long discountValue;
    private final long minimumPrice;

    public CouponEntity(final String name, final String policyType, final long discountValue, final long minimumPrice) {
        this(null, name, policyType, discountValue, minimumPrice);
    }

    public CouponEntity(final Long id,
                        final String name,
                        final String policyType,
                        final long discountValue,
                        final long minimumPrice) {
        this.id = id;
        this.name = name;
        this.policyType = policyType;
        this.discountValue = discountValue;
        this.minimumPrice = minimumPrice;
    }

    public static CouponEntity from(final Coupon coupon) {
        return new CouponEntity(
                coupon.getName(),
                coupon.getDiscountPolicyType().name(),
                coupon.getDiscountValue(),
                coupon.getMinimumPriceValue()
        );
    }

    public Coupon toDomain() {
        return new Coupon(id, name, DiscountPolicyType.from(policyType), discountValue, Money.from(minimumPrice));
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

    public long getDiscountValue() {
        return discountValue;
    }

    public long getMinimumPrice() {
        return minimumPrice;
    }
}
