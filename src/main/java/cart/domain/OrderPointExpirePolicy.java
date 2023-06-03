package cart.domain;

import java.time.LocalDate;
import java.time.Month;

public class OrderPointExpirePolicy implements PointExpirePolicy {

    @Override
    public LocalDate calculateExpireDate(LocalDate createAt) {
        LocalDate createAtPlusThreeMonth = createAt.plusMonths(3);
        return createAtPlusThreeMonth
                .withDayOfMonth(createAtPlusThreeMonth.lengthOfMonth());
    }

    @Override
    public boolean isSoonExpireDate(LocalDate base, LocalDate other) {
        return other.getYear() == base.getYear() && other.getMonth() == base.getMonth();
    }
}
