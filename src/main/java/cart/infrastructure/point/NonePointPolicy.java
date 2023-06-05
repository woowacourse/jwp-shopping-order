package cart.infrastructure.point;

import cart.domain.price.Point;
import cart.domain.price.PointPolicy;
import cart.domain.price.Price;

public class NonePointPolicy implements PointPolicy {
    @Override
    public Point calculate(Price price) {
        return Point.ZERO;
    }
}
