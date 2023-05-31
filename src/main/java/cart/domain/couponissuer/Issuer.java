package cart.domain.couponissuer;

import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Orders;
import cart.repository.CouponRepository;

import java.util.Optional;

public class Issuer extends CouponIssuer{
    public Issuer(CouponRepository couponRepository) {
        super(couponRepository);
    }

    @Override
    public Optional<Coupon> issue(Member member, Orders orders) {
        setNext();
        return this.execute(member,orders);
    }

    @Override
    protected void setNext() {
        this.next =  new OverFiftyPriceCouponIssuer(couponRepository);
    }
}
