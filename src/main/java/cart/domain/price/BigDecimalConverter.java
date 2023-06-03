package cart.domain.price;

import java.math.BigDecimal;

public class BigDecimalConverter {

    public static BigDecimal convert(final int number) {
        return BigDecimal.valueOf(number);
    }

    public static BigDecimal convert(final double number) {
        return BigDecimal.valueOf(number);
    }
}
