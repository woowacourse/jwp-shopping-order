package cart.domain.discountpolicy;

import cart.domain.Coupon;
import cart.domain.Point;

public interface PaymentDiscountPolicy {
    int calculatePaymentAmount(final Coupon coupon, final Point point, final int totalAmount);
}
