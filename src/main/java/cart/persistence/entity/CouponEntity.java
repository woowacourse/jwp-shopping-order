package cart.persistence.entity;

import java.time.LocalDateTime;

public class CouponEntity {
    private final Long id;
    private final String name;
    private final Integer discountRate;
    private final Integer period;
    private final LocalDateTime expiredDate;

    public CouponEntity(final String name, final Integer discountRate, final Integer period,
                        final LocalDateTime expiredDate) {
        this(null, name, discountRate, period, expiredDate);
    }

    public CouponEntity(final Long id, final String name, final Integer discountRate, final Integer period,
                        final LocalDateTime expiredDate) {
        this.id = id;
        this.name = name;
        this.discountRate = discountRate;
        this.period = period;
        this.expiredDate = expiredDate;
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

    public LocalDateTime getExpiredDate() {
        return expiredDate;
    }
}
