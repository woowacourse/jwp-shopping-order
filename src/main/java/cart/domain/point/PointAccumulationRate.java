package cart.domain.point;

import java.util.Arrays;

public enum PointAccumulationRate {

    RANGE_1(0, 50_000, 0.05),
    RANGE_2(50_000, 100_000, 0.08),
    RANGE_3(100_000, Integer.MAX_VALUE, 0.1);

    private final int minPayment;
    private final int maxPayment;
    private final double rate;

    PointAccumulationRate(int minPayment, int maxPayment, double rate) {
        this.minPayment = minPayment;
        this.maxPayment = maxPayment;
        this.rate = rate;
    }

    public static PointAccumulationRate findBy(int payment) {
        return Arrays.stream(values())
                     .filter(pointAccumulationRate -> pointAccumulationRate.isBetweenRange(payment))
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException("잘못된 금액입니다."));
    }

    private boolean isBetweenRange(int payment) {
        return minPayment <= payment && payment < maxPayment;
    }

    public double getRate() {
        return rate;
    }
}
