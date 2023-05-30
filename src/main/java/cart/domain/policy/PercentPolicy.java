package cart.domain.policy;

import cart.domain.Percent;
import cart.domain.Price;

public class PercentPolicy implements DiscountPolicy {

    public static final String NAME = "percent";
    private final Percent percent;

    public PercentPolicy(int value) {
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
