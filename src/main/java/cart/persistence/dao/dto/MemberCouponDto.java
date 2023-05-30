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
    private final LocalDateTime expiredDate;
    private final LocalDateTime issuedDate;
    private final boolean isUsed;

    public MemberCouponDto(final Long memberId, final String memberName, final String memberPassword,
                           final Long couponId, final String couponName, final int couponPeriod, final int discountRate,
                           final LocalDateTime expiredDate, final LocalDateTime issuedDate, final boolean isUsed) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberPassword = memberPassword;
        this.couponId = couponId;
        this.couponName = couponName;
        this.couponPeriod = couponPeriod;
        this.discountRate = discountRate;
        this.expiredDate = expiredDate;
        this.issuedDate = issuedDate;
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

    public LocalDateTime getExpiredDate() {
        return expiredDate;
    }

    public LocalDateTime getIssuedDate() {
        return issuedDate;
    }

    public boolean isUsed() {
        return isUsed;
    }
}
