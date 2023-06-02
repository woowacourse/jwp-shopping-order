package shop.persistence.entity.detail;

import java.time.LocalDateTime;

public class MemberCouponDetail {
    private final Long id;
    private final Long memberId;
    private final String memberName;
    private final String password;
    private final Long couponId;
    private final String couponName;
    private final Integer discountRate;
    private final Integer period;
    private final LocalDateTime issuedAt;
    private final LocalDateTime couponExpiredAt;
    private final LocalDateTime memberCouponExpiredAt;
    private final Boolean isUsed;

    public MemberCouponDetail(Long id, Long memberId, String memberName, String password, Long couponId,
                              Integer period, String couponName, Integer discountRate, LocalDateTime issuedAt,
                              LocalDateTime couponExpiredAt, LocalDateTime memberCouponExpiredAt, Boolean isUsed) {
        this.id = id;
        this.memberId = memberId;
        this.memberName = memberName;
        this.password = password;
        this.couponId = couponId;
        this.period = period;
        this.couponName = couponName;
        this.discountRate = discountRate;
        this.issuedAt = issuedAt;
        this.couponExpiredAt = couponExpiredAt;
        this.memberCouponExpiredAt = memberCouponExpiredAt;
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

    public String getPassword() {
        return password;
    }

    public Long getCouponId() {
        return couponId;
    }

    public String getCouponName() {
        return couponName;
    }

    public Integer getDiscountRate() {
        return discountRate;
    }

    public Integer getPeriod() {
        return period;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public LocalDateTime getCouponExpiredAt() {
        return couponExpiredAt;
    }

    public LocalDateTime getMemberCouponExpiredAt() {
        return memberCouponExpiredAt;
    }

    public Boolean isUsed() {
        return isUsed;
    }
}
