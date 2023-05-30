package cart.domain.coupon.dto;

import java.time.LocalDateTime;

public class CouponWithId {

    private final Long id;
    private final String name;
    private final Integer discountRate;
    private final Integer period;
    private final LocalDateTime expiredDate;

    public CouponWithId(final Long id, final String name, final Integer discountRate, final Integer period,
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

    public Integer getPeriod() {
        return period;
    }

    public Integer getDiscountRate() {
        return discountRate;
    }

    public LocalDateTime getExpiredDate() {
        return expiredDate;
    }
}
