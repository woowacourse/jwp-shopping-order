package cart.domain.coupon.discountpolicy;

import cart.domain.Money;
import java.math.BigDecimal;

public class NoneDiscountPolicy implements DiscountPolicy {

    @Override
    public boolean isValid(BigDecimal discountValue) {
        return discountValue.compareTo(BigDecimal.ZERO) == 0;
    }

    @Override
    public Money discount(Money totalPrice, BigDecimal discountValue) {
        return totalPrice;
    }
}
