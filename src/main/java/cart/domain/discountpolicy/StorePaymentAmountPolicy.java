package cart.domain.discountpolicy;

import cart.domain.Coupon;
import cart.domain.Point;

import java.util.List;

public class StorePaymentAmountPolicy {
    private final List<PaymentDiscountPolicy> paymentDiscountPolicies;

    public StorePaymentAmountPolicy() {
        this.paymentDiscountPolicies = List.of(new CouponDiscountPolicy(), new PointDiscountPolicy());
    }

    public int calculateFare(final Coupon coupon, final Point point, final int totalAmount) {
        int calculatedFare = totalAmount;
        for (PaymentDiscountPolicy paymentDiscountPolicy : paymentDiscountPolicies) {
            calculatedFare = paymentDiscountPolicy.calculatePaymentAmount(coupon, point, totalAmount);
        }
        return calculatedFare;
    }
}
