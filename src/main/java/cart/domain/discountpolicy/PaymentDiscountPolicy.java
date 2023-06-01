package cart.domain.discountpolicy;

import cart.domain.Coupon;
import cart.domain.Point;

import java.util.List;

public interface PaymentDiscountPolicy {

    int calculatePaymentAmount(final int totalAmount, final List<Coupon> coupons, final Point point);

}
