package cart.domain.coupon;

import cart.domain.member.Member;
import cart.exception.AuthorizationException;

import java.time.LocalDateTime;
import java.util.Objects;

import static cart.exception.ErrorCode.NOT_AUTHORIZATION_MEMBER;

public class MemberCoupon {

    private Long id;
    private final Member member;
    private final Coupon coupon;
    private final boolean isUsed;
    private final LocalDateTime issuedAt;
    private final LocalDateTime expiredAt;

    public MemberCoupon(final Member member, final Coupon coupon) {
        this.member = member;
        this.coupon = coupon;
        this.isUsed = false;
        this.issuedAt = LocalDateTime.now();
        this.expiredAt = calculateExpiredDate(coupon);
    }

    public MemberCoupon(final Long id, final Member member, final Coupon coupon, final boolean isUsed, final LocalDateTime issuedAt, final LocalDateTime expiredAt) {
        this.id = id;
        this.member = member;
        this.coupon = coupon;
        this.isUsed = isUsed;
        this.issuedAt = issuedAt;
        this.expiredAt = expiredAt;
    }

    private LocalDateTime calculateExpiredDate(final Coupon coupon) {
        LocalDateTime memberCouponExpiredDate = issuedAt.plusDays(coupon.getPeriod());
        if (memberCouponExpiredDate.isBefore(coupon.getExpiredAt())) {
            return memberCouponExpiredDate;
        }
        return coupon.getExpiredAt();
    }

    public void checkOwner(final Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new AuthorizationException(NOT_AUTHORIZATION_MEMBER);
        }
    }

    public boolean canUse() {
        return !isUsed;
    }

    public MemberCoupon markAsUsed() {
        return new MemberCoupon(id, member, coupon, true, issuedAt, expiredAt);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }
}
