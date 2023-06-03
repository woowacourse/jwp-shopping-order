package cart.domain.discountpolicy;

import cart.domain.coupon.Coupons;
import cart.domain.point.Point;

public interface DiscountPolicy {

    int calculatePayment(final int totalPrice, final Coupons coupons, final Point point);
}
