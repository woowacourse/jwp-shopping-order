package cart.domain.coupon;

import java.time.LocalDate;

public class Coupon {
    private final CouponName couponName;
    private final DiscountRate discountRate;
    private final Period period;
    private final LocalDate expiredDate;

    public Coupon(CouponName couponName, DiscountRate discountRate, Period period, LocalDate expiredDate) {
        this.couponName = couponName;
        this.discountRate = discountRate;
        this.period = period;
        this.expiredDate = expiredDate;
    }

    public String getCouponName() {
        return couponName.getName();
    }

    public int getDiscountRate() {
        return discountRate.getDiscountRate();
    }

    public int getPeriod() {
        return period.getPeriod();
    }

    public LocalDate getExpiredDate() {
        return expiredDate;
    }
}
