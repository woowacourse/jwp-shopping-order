package cart.domain.coupon;

import org.springframework.util.ObjectUtils;

import java.util.Objects;

public class UsageStatus {

    private final Boolean usageStatus;

    private UsageStatus(final Boolean usageStatus) {
        validate(usageStatus);
        this.usageStatus = usageStatus;
    }

    public static UsageStatus from(final Boolean usageStatus) {
        return new UsageStatus(usageStatus);
    }

    private void validate(final Boolean usageStatus) {
        if (ObjectUtils.isEmpty(usageStatus)) {
            throw new IllegalStateException("쿠폰 사용 상태는 빈 칸일 수 없습니다.");
        }
    }

    public boolean isNotUsed() {
        return !usageStatus;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final UsageStatus that = (UsageStatus) o;
        return Objects.equals(usageStatus, that.usageStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usageStatus);
    }

    public Boolean getUsageStatus() {
        return usageStatus;
    }
}
