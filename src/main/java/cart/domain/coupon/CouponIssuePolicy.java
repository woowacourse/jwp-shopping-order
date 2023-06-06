package cart.domain.coupon;

import cart.domain.Member;
import cart.domain.order.Order;

import java.util.Optional;

public abstract class CouponIssuePolicy {

    public static CouponIssuePolicy of(final Member member) {
        return new CouponIssuePolicyImpl();
    }

    abstract public Optional<Coupon> IssueCoupon(final Member member, final Order order);
}
