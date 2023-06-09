package cart.domain.discountpolicy;

import cart.domain.coupon.Coupons;
import cart.domain.point.Point;

public class CouponDiscountPolicy implements DiscountPolicy {

    @Override
    public int calculatePayment(final int totalPrice, final Coupons coupons, final Point point) {
        return coupons.getCoupons().stream()
                .reduce(totalPrice, (price, coupon) -> coupon.applyDiscount(price), Integer::min);
    }
}
