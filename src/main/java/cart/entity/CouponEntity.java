package cart.entity;

import cart.domain.VO.Money;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.DiscountPolicyType;

public class CouponEntity {

    private final Long id;
    private final String name;
    private final String policyType;
    private final long discountValue;
    private final long minimumPrice;
    private final boolean used;
    private final Long memberId;

    public CouponEntity(
            final String name,
            final String policyType,
            final long discountValue,
            final long minimumPrice,
            final boolean used,
            final Long memberId
    ) {
        this(null, name, policyType, discountValue, minimumPrice, used, memberId);
    }

    public CouponEntity(
            final Long id,
            final String name,
            final String policyType,
            final long discountValue,
            final long minimumPrice,
            final boolean used,
            final Long memberId
    ) {
        this.id = id;
        this.name = name;
        this.policyType = policyType;
        this.discountValue = discountValue;
        this.minimumPrice = minimumPrice;
        this.used = used;
        this.memberId = memberId;
    }

    public static CouponEntity from(final Coupon coupon) {
        return new CouponEntity(
                coupon.getId(),
                coupon.getName(),
                coupon.getDiscountPolicyType().name(),
                coupon.getDiscountValue(),
                coupon.getMinimumPriceValue(),
                coupon.isUsed(),
                coupon.getMemberId()
        );
    }

    public Coupon toDomain() {
        return new Coupon(
                id,
                name,
                DiscountPolicyType.from(policyType),
                discountValue,
                Money.from(minimumPrice),
                used,
                memberId
        );
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

    public boolean isUsed() {
        return used;
    }

    public Long getMemberId() {
        return memberId;
    }
}
