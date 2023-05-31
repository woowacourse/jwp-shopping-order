package cart.domain.couponissuer;

import cart.domain.Coupon;
import cart.domain.Orders;
import cart.repository.CouponRepository;

import java.util.Optional;

public class OverFiftyPriceCouponIssuer extends CouponIssuer {
    private static final long COUPON_ID = 1L;

    public OverFiftyPriceCouponIssuer(CouponRepository couponRepository) {
        super(couponRepository);
    }

    @Override
    public Optional<Coupon> issue(Orders orders) {
        if(orders.getDiscountPriceValue()>5000){
            this.issueCoupon(orders.getMemberId(),COUPON_ID);
            return Optional.of(couponRepository.findById(COUPON_ID));
        }
        setNext();
        return this.execute(orders);
    }

    @Override
    protected void setNext() {
        this.next = new NoCouponIssuer(couponRepository);
    }
}
