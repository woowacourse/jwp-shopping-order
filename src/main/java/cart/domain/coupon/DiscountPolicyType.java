package cart.domain.coupon;

import cart.exception.DiscountPolicyNotFoundException;
import java.util.Arrays;

public enum DiscountPolicyType {
    PRICE,
    PERCENT,
    DELIVERY,
    ;

    public static DiscountPolicyType from(final String name) {
        return Arrays.stream(values())
                .filter(discountPolicyType -> discountPolicyType.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(DiscountPolicyNotFoundException::new);
    }
}
