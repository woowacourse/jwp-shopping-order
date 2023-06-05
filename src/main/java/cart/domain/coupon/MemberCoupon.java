package cart.domain.coupon;

import cart.dto.MemberInfo;
import cart.exception.CouponException;
import cart.exception.ExceptionType;
import java.time.LocalDate;

public class MemberCoupon {

    private static final int DEFAULT_DATE_PERIOD = 3;

    private final Long id;
    private final MemberInfo member;
    private final Coupon coupon;
    private final LocalDate expiredDate;

    public MemberCoupon(MemberInfo member, Coupon coupon) {
        this(null, member, coupon, makeDefaultExpiredDate());
    }

    public MemberCoupon(MemberInfo member, Coupon coupon, LocalDate expiredDate) {
        this(null, member, coupon, expiredDate);
    }

    public MemberCoupon(Long id, MemberInfo member, Coupon coupon, LocalDate expiredDate) {
        this.id = id;
        this.member = member;
        this.coupon = coupon;
        this.expiredDate = expiredDate;
    }

    private static LocalDate makeDefaultExpiredDate() {
        return LocalDate.now().plusDays(DEFAULT_DATE_PERIOD);
    }

    public void check(MemberInfo member) {
        if (coupon.isCoupon()) {
            checkExpiredDate();
            checkOwner(member);
        }
    }

    private void checkExpiredDate() {
        if (!isNotExpired()) {
            throw new CouponException(ExceptionType.INVALID_EXPIRED_DATE);
        }
    }

    private void checkOwner(MemberInfo member) {
        if (!this.member.equals(member)) {
            throw new CouponException(ExceptionType.NO_AUTHORITY_COUPON);
        }
    }

    public boolean isNotExpired() {
        return !expiredDate.isBefore(LocalDate.now());
    }

    public boolean isExists() {
        return coupon.isCoupon();
    }

    public Long getId() {
        return id;
    }

    public MemberInfo getMember() {
        return member;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public LocalDate getExpiredDate() {
        return expiredDate;
    }
}
