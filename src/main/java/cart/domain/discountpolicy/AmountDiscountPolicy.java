package cart.domain.discountpolicy;

import cart.domain.Money;
import org.springframework.stereotype.Component;

@Component
public class AmountDiscountPolicy implements DiscountPolicy {

    @Override
    public Money apply(final Money original, final double value) {
        Money discountMoney = new Money((int) value);
        return original.subtract(discountMoney);
    }
}
