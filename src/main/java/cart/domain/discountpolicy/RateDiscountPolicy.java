package cart.domain.discountpolicy;

import cart.domain.Money;
import org.springframework.stereotype.Component;

@Component
public class RateDiscountPolicy implements DiscountPolicy {

    @Override
    public Money apply(Money original, double value) {
        return new Money((int) (original.getValue() * value));
    }

    @Override
    public boolean isSupportTyp(final DiscountType type) {
        return type == DiscountType.RATE;
    }
}
