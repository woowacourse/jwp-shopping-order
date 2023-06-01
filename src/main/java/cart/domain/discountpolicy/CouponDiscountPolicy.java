package cart.domain.discountpolicy;

import cart.domain.Coupon;
import cart.domain.Point;

import java.util.List;

public class CouponDiscountPolicy implements PaymentDiscountPolicy{

    @Override
    public int calculatePaymentAmount(final int totalAmount, final List<Coupon> coupons, final Point point) {
        return coupons.stream()
                .reduce(totalAmount, (amount, coupon) ->
                        coupon.applyDiscount(amount), Integer::min);
    }

}
