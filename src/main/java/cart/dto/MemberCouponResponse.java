package cart.dto;

import java.time.LocalDateTime;

public class MemberCouponResponse {

    private Long id;
    private String name;
    private int discountRate;
    private LocalDateTime expiredAt;
    private boolean isUsed;

    public MemberCouponResponse() {
    }

    public MemberCouponResponse(final Long id, final String name, final int discountRate, final LocalDateTime expiredAt, final boolean isUsed) {
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

    public int getDiscountRate() {
        return discountRate;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public boolean isUsed() {
        return isUsed;
    }
}
