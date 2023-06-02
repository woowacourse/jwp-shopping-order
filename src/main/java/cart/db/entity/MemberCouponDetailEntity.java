package cart.db.entity;

import java.time.LocalDateTime;

public class MemberCouponDetailEntity {

    private final Long id;
    private final Long memberId;
    private final String memberName;
    private final String memberPassword;
    private final Long couponId;
    private final String couponName;
    private final int discountRate;
    private final int period;
    private final LocalDateTime expiredAt;
    private final LocalDateTime issuedAtToMember;
    private final LocalDateTime expiredAtToMember;
    private final Boolean isUsed;

    public MemberCouponDetailEntity(
            final Long id,
            final Long memberId,
            final String memberName,
            final String memberPassword,
            final Long couponId,
            final String couponName,
            final int discountRate,
            final int period,
            final LocalDateTime expiredAt,
            final LocalDateTime issuedAtToMember,
            final LocalDateTime expiredAtToMember,
            final Boolean isUsed
    ) {
        this.id = id;
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberPassword = memberPassword;
        this.couponId = couponId;
        this.couponName = couponName;
        this.discountRate = discountRate;
        this.period = period;
        this.expiredAt = expiredAt;
        this.issuedAtToMember = issuedAtToMember;
        this.expiredAtToMember = expiredAtToMember;
        this.isUsed = isUsed;
    }

    public Long getId() {
        return id;
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

    public int getDiscountRate() {
        return discountRate;
    }

    public int getPeriod() {
        return period;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public LocalDateTime getIssuedAtToMember() {
        return issuedAtToMember;
    }

    public LocalDateTime getExpiredAtToMember() {
        return expiredAtToMember;
    }

    public Boolean getUsed() {
        return isUsed;
    }
}
