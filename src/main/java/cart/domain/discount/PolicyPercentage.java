package cart.domain.discount;

public class PolicyPercentage implements Policy {

    private final int value;

    public PolicyPercentage(final int value) {
        this.value = value;
    }

    @Override
    public int calculate(final int price) {
        return (int) (price - (price * value * 0.01));
    }
}
