package cart.domain.couponissuer;

import cart.domain.Coupon;
import cart.domain.Orders;
import cart.repository.CouponRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

// TODO : 쿠폰 지급 방법 고민해보기
public abstract class CouponIssuer {
    protected final CouponRepository couponRepository;
    protected CouponIssuer next;

    public CouponIssuer(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    abstract public Optional<Coupon> issue(final Orders orders);
    abstract protected void setNext() throws IllegalAccessException;
    protected Optional<Coupon> execute(Orders orders){
        return next.issue(orders);
    };
    protected void issueCoupon(final long memberId,final long couponId){
        couponRepository.issueCoupon(memberId,couponId);
    }
}
