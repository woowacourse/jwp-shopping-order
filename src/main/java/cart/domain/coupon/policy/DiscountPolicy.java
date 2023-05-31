package cart.domain.coupon.policy;

import java.math.BigDecimal;

public interface DiscountPolicy {

    BigDecimal calculatePrice(final Long price);

    Long getDiscountPrice();

    DiscountPolicyType getPolicyType();
}
