package cart.domain.pay;

import cart.domain.vo.Money;

import java.math.BigDecimal;
import java.util.function.Function;

public enum PayPoint {
    DEFAULT_POINT_POLICY((totalPay) -> totalPay.times(BigDecimal.valueOf(0.01)));

    private final Function<Money, Money> pointCalculator;

    PayPoint(Function<Money, Money> pointCalculator) {
        this.pointCalculator = pointCalculator;
    }

    public Money calculate(Money totalPay) {
        return pointCalculator.apply(totalPay);
    }
}
