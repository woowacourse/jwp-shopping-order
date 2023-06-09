package cart.application.dto.coupon;

import java.time.LocalDateTime;

public class IssueCouponRequest {

    private LocalDateTime expiredAt;

    public IssueCouponRequest() {
    }

    public IssueCouponRequest(final LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }
}
