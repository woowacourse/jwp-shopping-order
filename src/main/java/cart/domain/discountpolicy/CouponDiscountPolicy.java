package cart.domain.discountpolicy;

import cart.domain.Coupon;
import cart.domain.Point;

public class CouponDiscountPolicy implements PaymentDiscountPolicy{
    @Override
    public int calculatePaymentAmount(final Coupon coupon, final Point point, final int totalAmount) {
        return 0;
    }
}
