package cart.entity;

import cart.domain.coupon.Coupon;

import java.util.Objects;

public class CouponEntity {

    private final Long id;
    private final String name;
    private final String discountType;
    private final int amount;

    public CouponEntity(final Long id, final String name, final String discountType, final int amount) {
        this.id = id;
        this.name = name;
        this.discountType = discountType;
        this.amount = amount;
    }

    public CouponEntity(final String name, final String discountType, final int amount) {
        this.id = null;
        this.name = name;
        this.discountType = discountType;
        this.amount = amount;
    }

    public static CouponEntity from(Coupon coupon) {
        return new CouponEntity(
                coupon.getId(),
                coupon.getName(),
                coupon.getDiscount().getStrategy().getStrategyName().name(),
                coupon.getDiscount().getAmount()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDiscountType() {
        return discountType;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CouponEntity that = (CouponEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
