package cart.application.dto.coupon;

import java.time.LocalDateTime;

public class CouponResponse {

    private final Long id;
    private final String name;
    private final Integer discountRate;
    private final LocalDateTime expiredDate;

    public CouponResponse(final Long id, final String name, final Integer discountRate,
                          final LocalDateTime expiredDate) {
        this.id = id;
        this.name = name;
        this.discountRate = discountRate;
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

    public LocalDateTime getExpiredDate() {
        return expiredDate;
    }
}
