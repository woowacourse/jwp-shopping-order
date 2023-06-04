package cart.infrastructure.point;

import cart.domain.price.Point;
import cart.domain.price.PointPolicy;
import cart.domain.price.Price;

public class FixedAmountPointPolicy implements PointPolicy {
    private final long amount;

    public FixedAmountPointPolicy(long amount) {
        validateAmount(amount);
        this.amount = amount;
    }

    private void validateAmount(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("음수는 입력될 수 없습니다.");
        }
    }

    @Override
    public Point calculate(Price price) {
        return new Point(amount);
    }
}
