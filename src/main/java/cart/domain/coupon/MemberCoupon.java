package cart.domain.coupon;

import cart.domain.member.Member;

import java.time.LocalDateTime;

public class MemberCoupon {
    private final Long id;
    private final Member owner;
    private final Coupon coupon;
    private final LocalDateTime issuedAt;
    private final LocalDateTime expiredAt;
    private final Boolean isUsed;

    public MemberCoupon(Long id, Member owner, Coupon coupon, LocalDateTime issuedAt,
                        LocalDateTime expiredAt, Boolean isUsed) {
        this.id = id;
        this.owner = owner;
        this.coupon = coupon;
        this.issuedAt = issuedAt;
        this.expiredAt = expiredAt;
        this.isUsed = isUsed;
    }

    public static MemberCoupon issue(Member owner, Coupon coupon) {
        LocalDateTime issuedAt = LocalDateTime.now();
        int period = coupon.getPeriod();
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(period);

        return new MemberCoupon(null, owner, coupon, issuedAt, expiredAt, Boolean.FALSE);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return owner.getId();
    }

    public Long getCouponId() {
        return coupon.getId();
    }

    public LocalDateTime getCouponExpiredAt() {
        return coupon.getExpiredAt();
    }

    public String getCouponName(){
        return coupon.getName();
    }

    public Integer getDiscountRate(){
        return coupon.getDiscountRate();
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public LocalDateTime getMemberCouponExpiredAt() {
        return expiredAt;
    }

    public Boolean isUsed() {
        return isUsed;
    }
}
