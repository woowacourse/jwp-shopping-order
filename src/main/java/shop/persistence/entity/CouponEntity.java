package shop.persistence.entity;

import java.time.LocalDateTime;

public class CouponEntity {
    private final Long id;
    private final String name;
    private final Integer discountRate;
    private final Integer period;
    private final LocalDateTime expiredAt;

    public CouponEntity(Long id, String name, Integer discountRate,
                        Integer period, LocalDateTime expiredAt) {
        this.id = id;
        this.name = name;
        this.discountRate = discountRate;
        this.period = period;
        this.expiredAt = expiredAt;
    }

    public CouponEntity(String name, Integer discountRate, Integer period,
                        LocalDateTime expiredAt) {
        this(null, name, discountRate, period, expiredAt);
    }

    public Long getId() {
        return id;
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
