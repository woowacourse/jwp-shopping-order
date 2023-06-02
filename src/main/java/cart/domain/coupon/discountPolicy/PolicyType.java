package cart.domain.coupon.discountPolicy;

import cart.exception.coupon.PolicyTypeNotFoundException;

import java.util.Arrays;

public enum PolicyType {
    PRICE("PRICE"),
    PERCENT("PERCENT"),
    DELIVERY("DELIVERY");

    private final String name;

    PolicyType(final String name) {
        this.name = name;
    }

    public static PolicyType from(final String name) {
        return Arrays.stream(values())
                .filter(type -> type.name.equals(name))
                .findAny()
                .orElseThrow(PolicyTypeNotFoundException::new);
    }

    public String getName() {
        return name;
    }
}
