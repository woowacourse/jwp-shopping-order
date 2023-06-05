package cart.domain.couponissuer;

import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Orders;
import cart.repository.CouponRepository;

import java.util.Optional;

public class OverTwentyPriceCouponIssuer extends CouponIssuer {
    private static final long COUPON_ID = 2L;

    public OverTwentyPriceCouponIssuer(CouponRepository couponRepository) {
        super(couponRepository);
    }

    @Override
    public Optional<Coupon> issue(Member member, Orders orders) throws IllegalAccessException {
        if (orders.getPrice() > 2000) {
            this.issueCoupon(member.getId(), COUPON_ID);
            return Optional.of(couponRepository.findById(COUPON_ID));
        }
        return next.execute(member, orders);
    }

    @Override
    protected void setNext() {
        this.next = new NoCouponIssuer(couponRepository);
    }
}
