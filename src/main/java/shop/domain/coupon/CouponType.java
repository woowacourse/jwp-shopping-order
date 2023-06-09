package shop.domain.coupon;

import java.time.LocalDateTime;

public enum CouponType {
    WELCOME_JOIN("회원가입 감사 쿠폰", 10, 7,
            LocalDateTime.of(2023, 12, 31, 23, 59, 59));

    private final String name;
    private final int discountRate;
    private final int period;
    private final LocalDateTime expiredAt;

    CouponType(String name, int discountRate, int period, LocalDateTime expiredAt) {
        this.name = name;
        this.discountRate = discountRate;
        this.period = period;
        this.expiredAt = expiredAt;
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
