package cart.infrastructure.point;

import cart.domain.price.Point;
import cart.domain.price.PointPolicy;
import cart.domain.price.Price;

public class FixedPercentPointPolicy implements PointPolicy {
    private final double percent;

    public FixedPercentPointPolicy(double percent) {
        this.percent = percent;
    }

    @Override
    public Point calculate(Price price) {
        long amount = price.getAmount();
        double reward = amount * (percent / 100);
        return new Point((long) Math.ceil(reward));
    }
}
