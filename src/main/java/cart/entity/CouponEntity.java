package cart.entity;

import cart.domain.Coupon;
import cart.domain.vo.Amount;

public class CouponEntity {

    private final Long id;
    private final String name;
    private final int minAmount;
    private final int discountAmount;

    private CouponEntity(final Long id, final String name, final int minAmount, final int discountAmount) {
        this.id = id;
        this.name = name;
        this.minAmount = minAmount;
        this.discountAmount = discountAmount;
    }

    public static CouponEntity of(final long id, final String name, final int minAmount, final int discountAmount) {
        return new CouponEntity(id, name, minAmount, discountAmount);
    }

    public static CouponEntity from(final Coupon coupon) {
        return new CouponEntity(coupon.getId(), coupon.getName(), coupon.getMinAmount().getValue(),
                coupon.getDiscountAmount().getValue());
    }

    public Coupon toDomain() {
        return new Coupon(id, name, Amount.of(discountAmount), Amount.of(minAmount));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }
}
