package cart.domain.coupon.discountpolicy;

import cart.domain.Money;
import java.math.BigDecimal;

public class FixedDiscountPolicy implements DiscountPolicy {

    @Override
    public boolean isValid(BigDecimal discountValue) {
        boolean isGreaterThanZero = discountValue.compareTo(BigDecimal.ZERO) == 1;
        return isGreaterThanZero;
    }

    @Override
    public Money discount(Money totalPrice, BigDecimal discountValue) {
        if (totalPrice.isLessThan(discountValue)) {
            return Money.ZERO;
        }
        return totalPrice.subtract(discountValue);
    }
}
