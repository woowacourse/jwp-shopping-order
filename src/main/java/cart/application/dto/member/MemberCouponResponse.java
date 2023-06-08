package cart.application.dto.member;

import java.time.LocalDateTime;

public class MemberCouponResponse {

    private final Long id;
    private final String name;
    private final Integer discountRate;
    private final LocalDateTime expiredAt;
    private final boolean isUsed;

    public MemberCouponResponse(final Long id, final String name, final Integer discountRate,
                                final LocalDateTime expiredAt, final boolean isUsed) {
        this.id = id;
        this.name = name;
        this.discountRate = discountRate;
        this.expiredAt = expiredAt;
        this.isUsed = isUsed;
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

    public boolean getIsUsed() {
        return isUsed;
    }
}
