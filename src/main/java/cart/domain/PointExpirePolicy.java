package cart.domain;

import java.time.LocalDate;

public interface PointExpirePolicy {

    LocalDate calculateExpireDate(LocalDate createAt);
}
