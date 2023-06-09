package shop.application.coupon.dto;

import shop.domain.coupon.Coupon;

import java.time.LocalDateTime;

public class CouponDto {
    private final Long id;
    private final String name;
    private final int discountRate;
    private final int period;
    private final LocalDateTime expiredAt;

    private CouponDto(Long id, String name, int discountRate, int period, LocalDateTime expiredAt) {
        this.id = id;
        this.name = name;
        this.discountRate = discountRate;
        this.period = period;
        this.expiredAt = expiredAt;
    }

    public static CouponDto of(Coupon coupon) {
        return new CouponDto(
                coupon.getId(),
                coupon.getName(),
                coupon.getDiscountRate(),
                coupon.getPeriod(),
                coupon.getExpiredAt()
        );
    }

    public Long getId() {
        return id;
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
