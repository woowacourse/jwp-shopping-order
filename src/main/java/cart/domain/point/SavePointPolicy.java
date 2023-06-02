package cart.domain.point;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

public class SavePointPolicy {

    public static int calculate(int payment) {
        PointAccumulationRate pointAccumulationRate = PointAccumulationRate.findBy(payment);
        return (int) Math.floor(payment * pointAccumulationRate.getRate());
    }

    public static LocalDateTime getExpiredAt(LocalDateTime now) {
        LocalDateTime plus3Months = now.plusMonths(3);
        return plus3Months.with(TemporalAdjusters.firstDayOfMonth());
    }
}
