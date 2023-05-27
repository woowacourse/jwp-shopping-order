package cart.domain.coupon;

import cart.exception.DiscountConditionNotFoundException;
import java.util.Arrays;

public enum DiscountConditionType {
    MINIMUM_PRICE,
    NONE,
    ;

    public static DiscountConditionType from(final String name) {
        return Arrays.stream(values())
                .filter(discountConditionType -> discountConditionType.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(DiscountConditionNotFoundException::new);
    }
}
