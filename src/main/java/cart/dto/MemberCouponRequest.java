package cart.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class MemberCouponRequest {
    private LocalDateTime expiredAt;

    public MemberCouponRequest() {
    }

    public MemberCouponRequest(final LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MemberCouponRequest that = (MemberCouponRequest) o;
        return Objects.equals(expiredAt, that.expiredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expiredAt);
    }

    @Override
    public String toString() {
        return "MemberCouponRequest{" +
                "expiredAt=" + expiredAt +
                '}';
    }
}
