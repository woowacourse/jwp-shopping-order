package cart.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DefaultPointManager implements PointManager {

    private static final double EARNING_RATE = 0.05;
    private static final int EXPIRED_MONTH = 6;

    @Override
    public Point getPoint(final Price price) {
        return Point.valueOf(calculate(price.getValue()));
    }

    private int calculate(final int value) {
        return (int) (EARNING_RATE * value);
    }

    @Override
    public Timestamp getExpiredAt(final Timestamp createdAt) {
        final LocalDateTime localDateTime = createdAt.toLocalDateTime();
        final LocalDateTime expiredAt = localDateTime.plusMonths(EXPIRED_MONTH);
        return Timestamp.valueOf(expiredAt);
    }

    @Override
    public double getEarningRate() {
        return EARNING_RATE * 100;
    }
}
