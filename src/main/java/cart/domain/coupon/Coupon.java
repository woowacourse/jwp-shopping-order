package cart.domain.coupon;

import java.time.LocalDateTime;

public class Coupon {

    private final Long couponId;
    private final CouponName name;
    private final CouponDiscountRate discountRate;
    private final CouponPeriod period;
    private final LocalDateTime expiredAt;

    private Coupon(final CouponName name, final CouponDiscountRate discountRate,
                   final CouponPeriod period, final LocalDateTime expiredAt) {
        this(null, name, discountRate, period, expiredAt);
    }

    private Coupon(final Long couponId, final CouponName name, final CouponDiscountRate discountRate,
                   final CouponPeriod period, final LocalDateTime expiredAt) {
        this.couponId = couponId;
        this.name = name;
        this.discountRate = discountRate;
        this.period = period;
        this.expiredAt = expiredAt;
    }

    public static Coupon create(final String name, final int discountRate,
                                final int period, final LocalDateTime expiredAt) {
        return new Coupon(
            CouponName.create(name),
            CouponDiscountRate.create(discountRate),
            CouponPeriod.create(period),
            expiredAt);
    }

    public static Coupon create(final Long couponId, final String name, final int discountRate,
                                final int period, final LocalDateTime expiredAt) {
        return new Coupon(
            couponId,
            CouponName.create(name),
            CouponDiscountRate.create(discountRate),
            CouponPeriod.create(period),
            expiredAt);
    }

    public Long couponId() {
        return couponId;
    }

    public String name() {
        return name.getName();
    }

    public int discountRate() {
        return discountRate.getDiscountRate();
    }

    public int period() {
        return period.getPeriod();
    }

    public LocalDateTime expiredAt() {
        return expiredAt;
    }

    public boolean isNotExpired(final LocalDateTime targetTime) {
        return expiredAt.isAfter(targetTime);
    }
}
