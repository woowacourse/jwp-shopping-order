package cart.domain.coupon.discountCondition;

import cart.exception.ConditionTypeNotFoundException;

import java.util.Arrays;

public enum ConditionType {
    MINIMUM_PRICE("MINIMUM_PRICE"),
    NONE("NONE");

    private final String name;

    ConditionType(final String name) {
        this.name = name;
    }

    public static ConditionType from(final String name) {
        return Arrays.stream(values())
                .filter(type -> type.name.equals(name))
                .findAny()
                .orElseThrow(ConditionTypeNotFoundException::new);
    }
}
