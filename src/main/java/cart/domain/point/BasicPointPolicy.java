package cart.domain.point;

import cart.domain.common.Money;

public class BasicPointPolicy implements PointPolicy {

    @Override
    public Point save(final Money payment) {
        return new Point(payment.getPartial(0.1));
    }
}
