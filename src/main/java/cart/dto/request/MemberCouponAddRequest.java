package cart.dto.request;

import java.time.LocalDateTime;

public class MemberCouponAddRequest {
    private LocalDateTime expiredAt;

    public MemberCouponAddRequest() {
    }

    public MemberCouponAddRequest(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }
}
