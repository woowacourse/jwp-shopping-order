package cart.domain.coupon;

import cart.domain.Member;
import cart.domain.order.Order;

import java.util.Optional;

public class CouponIssuePolicyImpl extends CouponIssuePolicy {
    private static final int COUPON_ISSUE_PRICE = 100_000;

    @Override
    public Optional<Coupon> IssueCoupon(final Member member, final Order order) {
        if (order.price() >= COUPON_ISSUE_PRICE) {
            return Optional.of(new Coupon(2000, "2000원 할인 구폰"));
        }
        return Optional.empty();
    }
}
