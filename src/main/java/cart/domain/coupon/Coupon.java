package cart.domain.coupon;

import java.time.LocalDateTime;

public class Coupon {
    private final Long id;
    private final CouponName name;
    private final DiscountRate discountRate;
    private final Period period;
    private final LocalDateTime expiredAt;

    public Coupon(Long id, String name, int discountRate, int period, LocalDateTime expiredAt) {
        this.id = id;
        this.name = new CouponName(name);
        this.discountRate = new DiscountRate(discountRate);
        this.period = new Period(period);
        this.expiredAt = expiredAt;
    }

    public Long getId() {
        return id;
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

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }
}
