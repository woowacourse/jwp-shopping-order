package cart.domain.couponissuer;

import cart.domain.Coupon;
import cart.domain.Orders;
import cart.repository.CouponRepository;

import java.util.Optional;

public class NoCouponIssuer extends CouponIssuer{

    public NoCouponIssuer(CouponRepository couponRepository) {
        super(couponRepository);
    }

    @Override
    public Optional<Coupon> issue(Orders orders) {
        return Optional.empty();
    }

    @Override
    protected void setNext() throws IllegalAccessException {
        throw new IllegalAccessException("지급되는 쿠폰이 없는 경우는 마지막 경우입니다.");
    }
}
