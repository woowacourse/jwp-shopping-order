package cart.domain.coupon.discountpolicy;

import cart.domain.Money;
import java.math.BigDecimal;

public class RateDiscountPolicy implements DiscountPolicy {

    private static final BigDecimal UNIT = BigDecimal.valueOf(100);

    @Override
    public boolean isValid(BigDecimal discountValue) {
        boolean isGreaterThanZero = discountValue.compareTo(BigDecimal.ZERO) == 1;
        boolean isLessThanEqualUnit = discountValue.compareTo(UNIT) <= 0;
        return isGreaterThanZero && isLessThanEqualUnit;
    }

    @Override
    public Money discount(Money totalPrice, BigDecimal discountValue) {
        BigDecimal percentage = discountValue.divide(UNIT);
        return totalPrice.multiply(applyDiscountPercentage(percentage));
    }

    private BigDecimal applyDiscountPercentage(BigDecimal percent) {
        return BigDecimal.ONE.subtract(percent);
    }
}
