package cart.domain.coupon;

import java.time.LocalDateTime;

public class Coupon {

    private final CouponName name;
    private final CouponDiscountRate discountRate;
    private final CouponPeriod period;
    private final LocalDateTime expiredAt;

    private Coupon(final CouponName name, final CouponDiscountRate discountRate, final CouponPeriod period,
                   final LocalDateTime expiredAt) {
        this.name = name;
        this.discountRate = discountRate;
        this.period = period;
        this.expiredAt = expiredAt;
    }

    public static Coupon create(final String name, final int discountRate, final int period,
                                final LocalDateTime expiredAt) {
        return new Coupon(
            CouponName.create(name),
            CouponDiscountRate.create(discountRate),
            CouponPeriod.create(period),
            expiredAt);
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
}
