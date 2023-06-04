package cart.dto.request;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

public class MemberCouponAddRequest {
    @DateTimeFormat
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
