package cart.dto;

import cart.domain.Coupon;

import java.time.LocalDateTime;

public class CouponResponse {

    private String name;
    private int discountRate;
    private int period;
    private LocalDateTime expiredAt;

    public CouponResponse() {
    }

    public CouponResponse(final String name, final int discountRate, final int period, final LocalDateTime expiredAt) {
        this.name = name;
        this.discountRate = discountRate;
        this.period = period;
        this.expiredAt = expiredAt;
    }

    public CouponResponse(final Coupon coupon) {
        this.name = coupon.getName();
        this.discountRate = coupon.getDiscountRate();
        this.period = coupon.getPeriod();
        this.expiredAt = coupon.getExpiredDate();
    }

    public String getName() {
        return name;
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public int getPeriod() {
        return period;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }
}
