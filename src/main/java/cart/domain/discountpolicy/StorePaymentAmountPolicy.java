package cart.domain.discountpolicy;

import cart.domain.Coupon;
import cart.domain.Point;

import java.util.List;

public class StorePaymentAmountPolicy {
    private final List<PaymentDiscountPolicy> paymentDiscountPolicies;

    public StorePaymentAmountPolicy() {
        this.paymentDiscountPolicies = List.of(new CouponDiscountPolicy(), new PointDiscountPolicy());
    }

    public int calculateFare(final int totalAmount, final List<Coupon> coupons, final Point point) {
        int calculatedFare = totalAmount;
        for (PaymentDiscountPolicy paymentDiscountPolicy : paymentDiscountPolicies) {
            calculatedFare = paymentDiscountPolicy.calculatePaymentAmount(totalAmount, coupons, point);
        }
        return calculatedFare;
    }
}
