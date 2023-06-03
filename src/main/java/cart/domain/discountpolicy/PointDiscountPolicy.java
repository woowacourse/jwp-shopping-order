package cart.domain.discountpolicy;

import cart.domain.coupon.Coupons;
import cart.domain.point.Point;

public class PointDiscountPolicy implements DiscountPolicy {

    @Override
    public int calculatePayment(final int totalPrice, final Coupons coupons, final Point point) {
        return point.applyDisCount(totalPrice);
    }
}
