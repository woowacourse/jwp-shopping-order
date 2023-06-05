package cart.domain.couponissuer;

import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Orders;
import cart.repository.CouponRepository;

import java.util.Optional;


public abstract class CouponIssuer {
    protected final CouponRepository couponRepository;
    protected CouponIssuer next;

    public CouponIssuer(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    abstract public Optional<Coupon> issue(Member member, final Orders orders) throws IllegalAccessException;

    abstract protected void setNext() throws IllegalAccessException;

    protected Optional<Coupon> execute(Member member, Orders orders) throws IllegalAccessException {
        setNext();
        return next.issue(member, orders);
    }

    protected void issueCoupon(final long memberId, final long couponId) {
        couponRepository.issueCoupon(memberId, couponId);
    }
}
