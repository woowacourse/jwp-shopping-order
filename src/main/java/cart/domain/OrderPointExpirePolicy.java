package cart.domain;

import java.time.LocalDate;

public class OrderPointExpirePolicy implements PointExpirePolicy {

    @Override
    public LocalDate calculateExpireDate(LocalDate createAt) {
        LocalDate createAtPlusThreeMonth = createAt.plusMonths(3);
        return createAtPlusThreeMonth
                .withDayOfMonth(createAtPlusThreeMonth.lengthOfMonth());
    }
}
