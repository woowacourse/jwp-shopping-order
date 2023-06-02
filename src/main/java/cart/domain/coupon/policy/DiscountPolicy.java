package cart.domain.coupon.policy;

import cart.domain.Money;
import java.math.BigDecimal;

public interface DiscountPolicy {

    boolean isValid(BigDecimal discountValue);

    Money discount(Money totalPrice, BigDecimal discountValue);
}
