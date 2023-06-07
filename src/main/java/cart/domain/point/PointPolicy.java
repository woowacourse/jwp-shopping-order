package cart.domain.point;

import cart.domain.common.Money;

public interface PointPolicy {

    Point save(final Money payment);
}
