package cart.domain.discountpolicy;

import cart.domain.coupon.Coupon;

import java.util.List;

public class CouponDiscountPolicy implements PaymentDiscountPolicy{

    @Override
    public int calculatePaymentAmount(final int totalAmount, final List<Coupon> coupons, final int point) {
        return coupons.stream()
                .reduce(totalAmount, (amount, coupon) ->
                        coupon.applyDiscount(amount), Integer::min);
    }

}
