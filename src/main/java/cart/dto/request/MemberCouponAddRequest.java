package cart.dto.request;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;

public class MemberCouponAddRequest {
    @NotEmpty
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
