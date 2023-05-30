package cart.domain.policy;

import cart.domain.Percent;
import cart.domain.Price;
import cart.domain.policy.DiscountPolicy;

public class PercentPolicy implements DiscountPolicy {

    public static final String NAME = "percent";
    private final Percent percent;

    public PercentPolicy(int value) {
        this.percent = new Percent(value);
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
