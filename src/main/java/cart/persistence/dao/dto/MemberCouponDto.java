package cart.persistence.dao.dto;

import java.time.LocalDateTime;

public class MemberCouponDto {

    private final Long memberId;
    private final String memberName;
    private final String memberPassword;
    private final Long couponId;
    private final String couponName;
    private final int couponPeriod;
    private final int discountRate;
    private final LocalDateTime expiredAt;
    private final LocalDateTime issuedAt;
    private final boolean isUsed;

    public MemberCouponDto(final Long memberId, final String memberName, final String memberPassword,
                           final Long couponId, final String couponName, final int couponPeriod, final int discountRate,
                           final LocalDateTime expiredAt, final LocalDateTime issuedAt, final boolean isUsed) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberPassword = memberPassword;
        this.couponId = couponId;
        this.couponName = couponName;
        this.couponPeriod = couponPeriod;
        this.discountRate = discountRate;
        this.expiredAt = expiredAt;
        this.issuedAt = issuedAt;
        this.isUsed = isUsed;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getMemberPassword() {
        return memberPassword;
    }

    public Long getCouponId() {
        return couponId;
    }

    public String getCouponName() {
        return couponName;
    }

    public int getCouponPeriod() {
        return couponPeriod;
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public boolean isUsed() {
        return isUsed;
    }
}
