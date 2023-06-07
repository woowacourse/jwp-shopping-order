package cart.domain.couponissuer;

import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Orders;
import cart.repository.CouponRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CouponIssuerImpl extends CouponIssuer {
    public CouponIssuerImpl(CouponRepository couponRepository) {
        super(couponRepository);
    }

    @Override
    public Optional<Coupon> issue(Member member, Orders orders) throws IllegalAccessException {
        return this.execute(member, orders);
    }

    @Override
    protected void setNext() {
        this.next = new OverFiftyPriceCouponIssuer(couponRepository);
    }
}
