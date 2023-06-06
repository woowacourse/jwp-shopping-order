package cart.dto.request;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

public class MemberCouponAddRequest {
    @NotNull
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
