package cart.application.dto.coupon;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;

@JsonInclude(Include.NON_EMPTY)
public class CouponResponse {

    private final Long id;
    private final String name;
    private final Integer discountRate;
    private final LocalDateTime expiredAt;

    public CouponResponse(final String name, final Integer discountRate) {
        this(null, name, discountRate, null);
    }

    public CouponResponse(final Long id, final String name, final Integer discountRate,
                          final LocalDateTime expiredAt) {
        this.id = id;
        this.name = name;
        this.discountRate = discountRate;
        this.expiredAt = expiredAt;
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

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }
}
