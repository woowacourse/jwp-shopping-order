package cart.domain;

import cart.dao.entity.CouponTypeEntity;

public class CouponType {

    private final String name;
    private final DiscountType discountType;
    private final Money discountAmount;

    private CouponType(final String name, final DiscountType discountType, final Money discountAmount) {
        this.name = name;
        this.discountType = discountType;
        this.discountAmount = discountAmount;
    }

    public static CouponType from(final CouponTypeEntity couponType) {
        return new CouponType(
                couponType.getName(),
                DiscountType.find(couponType.getDiscountType()),
                new Money(couponType.getDiscountAmount()));
    }

    public String getName() {
        return name;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public Money getDiscountAmount() {
        return discountAmount;
    }
}
