package cart.domain.coupon;

import java.time.LocalDateTime;

public class Coupon {
    private final CouponName name;
    private final DiscountRate discountRate;
    private final Period period;
    private final LocalDateTime expiredDate;

    public Coupon(String name, int discountRate, int period, LocalDateTime expiredDate) {
        this.name = new CouponName(name);
        this.discountRate = new DiscountRate(discountRate);
        this.period = new Period(period);
        this.expiredDate = expiredDate;
    }

    public String getName() {
        return name.getName();
    }

    public int getDiscountRate() {
        return discountRate.getDiscountRate();
    }

    public int getPeriod() {
        return period.getPeriod();
    }

    public LocalDateTime getExpiredDate() {
        return expiredDate;
    }
}
