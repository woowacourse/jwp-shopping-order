package cart.domain.coupon;

import cart.domain.Price;
import cart.domain.policy.DiscountPolicy;

public interface Coupon {

    Price apply(Price price);

    Long getId();

    String getName();

    String getDiscountPolicyName();

    int getDiscountValue();

    String getRange();

}
