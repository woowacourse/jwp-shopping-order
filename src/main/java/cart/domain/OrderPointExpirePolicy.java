package cart.domain;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class OrderPointExpirePolicy implements PointExpirePolicy {

    private static final int MONTHS_TO_ADD = 3;

    @Override
    public LocalDate calculateExpireDate(LocalDate createAt) {
        LocalDate createAtPlusThreeMonth = createAt.plusMonths(MONTHS_TO_ADD);
        return createAtPlusThreeMonth.withDayOfMonth(createAtPlusThreeMonth.lengthOfMonth());
    }
}
