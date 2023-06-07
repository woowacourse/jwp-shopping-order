package cart.domain.coupon;

import cart.domain.Member;
import cart.domain.order.Order;

import java.util.Optional;

public interface CouponIssuePolicy {

    static CouponIssuePolicy of(final Member member) {
        return new CouponIssuePolicyImpl();
    }

    Optional<Coupon> issueCoupon(final Member member, final Order order);
}
