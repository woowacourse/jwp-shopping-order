package cart.domain;

import cart.dao.entity.CouponTypeEntity;

public class CouponType {

    private final long id;
    private final String name;
    private final DiscountType discountType;
    private final Money discountAmount;

    private CouponType(final long id, final String name, final DiscountType discountType, final Money discountAmount) {
        this.id = id;
        this.name = name;
        this.discountType = discountType;
        this.discountAmount = discountAmount;
    }

    public static CouponType from(final CouponTypeEntity couponType) {
        return new CouponType(
                couponType.getId(),
                couponType.getName(),
                DiscountType.find(couponType.getDiscountType()),
                new Money(couponType.getDiscountAmount()));
    }

    public long getId() {
        return id;
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
