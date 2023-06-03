package cart.domain;

import cart.domain.coupon.Coupon;
import cart.exception.IllegalCouponException;
import cart.exception.IllegalMemberException;
import java.time.LocalDate;

public class MemberCoupon {

    private static final int DEFAULT_DATE_PERIOD = 3;

    private final Long id;
    private final Member member;
    private final Coupon coupon;
    private final LocalDate expiredDate;

    public MemberCoupon(Member member, Coupon coupon) {
        this(null, member, coupon, makeDefaultExpiredDate());
    }

    public MemberCoupon(Member member, Coupon coupon, LocalDate expiredDate) {
        this(null, member, coupon, expiredDate);
    }

    public MemberCoupon(Long id, Member member, Coupon coupon, LocalDate expiredDate) {
        this.id = id;
        this.member = member;
        this.coupon = coupon;
        this.expiredDate = expiredDate;
    }

    private static LocalDate makeDefaultExpiredDate() {
        return LocalDate.now().plusDays(DEFAULT_DATE_PERIOD);
    }

    public void check(Member member) {
        if (coupon.isCoupon()) {
            checkExpiredDate();
            checkOwner(member);
        }
    }

    private void checkExpiredDate() {
        if (expiredDate.isBefore(LocalDate.now())) {
            throw new IllegalCouponException();
        }
    }

    private void checkOwner(Member member) {
        if (!this.member.equals(member)) {
            throw new IllegalMemberException();
        }
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

    public LocalDate getExpiredDate() {
        return expiredDate;
    }
}
