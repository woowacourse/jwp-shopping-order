package cart.domain.coupon;

import java.time.LocalDateTime;

public enum CouponType {
    WELCOME_JOIN("회원가입 감사 쿠폰", 10, 7,
            LocalDateTime.of(2023, 12, 31, 23, 59, 59));

    private final String name;
    private final Integer discountRate;
    private final Integer period;
    private final LocalDateTime expiredAt;

    CouponType(String name, Integer discountRate, Integer period, LocalDateTime expiredAt) {
        this.name = name;
        this.discountRate = discountRate;
        this.period = period;
        this.expiredAt = expiredAt;
    }

    public String getName() {
        return name;
    }

    public Integer getDiscountRate() {
        return discountRate;
    }

    public Integer getPeriod() {
        return period;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }
}
