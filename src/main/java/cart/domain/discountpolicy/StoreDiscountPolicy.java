package cart.domain.discountpolicy;

import cart.domain.coupon.Coupons;
import cart.domain.point.Point;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StoreDiscountPolicy {

    private final List<DiscountPolicy> discountPolicies;

    public StoreDiscountPolicy() {
        this.discountPolicies = List.of(new CouponDiscountPolicy(), new PointDiscountPolicy());
    }

    public int calculateStoreDiscount(final int totalPrice, final Coupons coupons, final Point point) {
        int paymentPrice = totalPrice;
        for (DiscountPolicy discountPolicy : discountPolicies) {
            paymentPrice = discountPolicy.calculatePayment(totalPrice, coupons, point);
        }
        return paymentPrice;
    }
}
