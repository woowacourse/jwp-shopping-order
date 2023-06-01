package cart.domain;

import java.time.LocalDate;
import java.util.Date;

public class DefaultPointExpirePolicy implements PointExpirePolicy {

    @Override
    public LocalDate calculateExpireDate(LocalDate createAt) {
        LocalDate createAtPlusThreeMonth = createAt.plusMonths(3);
        return createAtPlusThreeMonth
                .withDayOfMonth(createAtPlusThreeMonth.lengthOfMonth());
    }
}
