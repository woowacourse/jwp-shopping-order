package cart.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class MemberCoupon {
    private final Long id;
    private final Member member;
    private final Coupon coupon;
    private final LocalDateTime expiredAt;

    public MemberCoupon(Long id, Member member, Coupon coupon, Timestamp expiredAt) {
        this.id = id;
        this.member = member;
        this.coupon = coupon;
        this.expiredAt = expiredAt.toLocalDateTime();
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

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public boolean isAvailable(Money price) {
        return coupon.isAvailable(price);
    }
}
