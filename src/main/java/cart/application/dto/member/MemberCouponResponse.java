package cart.application.dto.member;

import java.time.LocalDateTime;

public class MemberCouponResponse {

    private final Long id;
    private final String name;
    private final Integer discountRate;
    private final LocalDateTime expiredDate;
    private final boolean isUsed;

    public MemberCouponResponse(final Long id, final String name, final Integer discountRate,
                                final LocalDateTime expiredDate, final boolean isUsed) {
        this.id = id;
        this.name = name;
        this.discountRate = discountRate;
        this.expiredDate = expiredDate;
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

    public LocalDateTime getExpiredDate() {
        return expiredDate;
    }

    public boolean getIsUsed() {
        return isUsed;
    }
}
