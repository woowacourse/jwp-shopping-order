package cart.domain.couponissuer;

import cart.domain.Coupon;
import cart.domain.Orders;
import cart.repository.CouponRepository;

import java.util.Optional;

public class Issuer extends CouponIssuer{
    public Issuer(CouponRepository couponRepository) {
        super(couponRepository);
    }

    @Override
    public Optional<Coupon> issue(Orders orders) {
        setNext();
        return this.execute(orders);
    }

    @Override
    protected void setNext() {
        this.next =  new OverFiftyPriceCouponIssuer(couponRepository);
    }
}
