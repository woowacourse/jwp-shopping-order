package fixture;

import cart.domain.Coupon;
import java.util.Optional;

public class CouponFixture {

    public static final Optional<Coupon> NOT_NULL_DISCOUNT_RATE_COUPON = Optional.of(new Coupon("쿠폰", 10d, 0));
    public static final Optional<Coupon> NOT_NULL_DISCOUNT_PRICE_COUPON = Optional.of(new Coupon("쿠폰", 0d, 5_000_000));
    public static final Optional<Coupon> NULL_COUPON = Optional.empty();

}
