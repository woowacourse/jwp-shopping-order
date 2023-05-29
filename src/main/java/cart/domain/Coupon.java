package cart.domain;

import java.time.LocalDateTime;

public class Coupon {

    private final String name;
    private final int discountRate;
    private final int period;
    private final LocalDateTime expiredDate;

    public Coupon(final String name, final int discountRate, final int period, final LocalDateTime expiredDate) {
        this.name = name;
        this.discountRate = discountRate;
        this.period = period;
        this.expiredDate = expiredDate;
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

    public LocalDateTime getExpiredDate() {
        return expiredDate;
    }
}
