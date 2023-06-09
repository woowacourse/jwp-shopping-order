package cart.domain;

import java.util.Arrays;
import java.util.Objects;

public enum DiscountType {

    DISCOUNT_PRICE("정액제");

    private final String value;

    DiscountType(final String value) {
        this.value = value;
    }

    public static DiscountType find(final String typeValue) {
        return Arrays.stream(values())
                .filter(value -> Objects.equals(value.value, typeValue))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Illegal coupon type value; value = " + typeValue));
    }
}
