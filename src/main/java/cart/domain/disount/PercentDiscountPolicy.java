package cart.domain.disount;

import cart.domain.value.Percent;
import cart.domain.value.Price;

public class PercentDiscountPolicy implements DiscountPolicy {

    public static final String NAME = "percent";
    private final Percent percent;

    public PercentDiscountPolicy(int value) {
        Percent percent = new Percent(value);
        validateIsZero(percent);
        this.percent = percent;
    }

    private void validateIsZero(Percent percent) {
        if (percent.isZero()) {
            throw new IllegalArgumentException("0퍼센트는 불가능 합니다.");
        }
    }

    @Override
    public Price discount(Price price) {
        return price.discount(percent);
    }

    @Override
    public int getValue() {
        return percent.getValue();
    }

    @Override
    public String getName() {
        return NAME;
    }
}
