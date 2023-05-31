package cart.domain;

import cart.domain.common.Money;

public interface PointPolicy {

    Point save(final Money payment);
}
