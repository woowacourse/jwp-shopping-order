package cart.domain.coupon.discountPolicy;

import cart.exception.PolicyTypeNotFoundException;

import java.util.Arrays;

public enum PolicyType {
    PRICE("PRICE", PricePolicy.class),
    PERCENT("PERCENT", PercentPolicy.class),
    DELIVERY("DELIVERY", DeliveryPolicy.class);

    private final String name;
    private final Class<? extends DiscountPolicy> policyClass;

    PolicyType(final String name, final Class<? extends DiscountPolicy> policyClass) {
        this.name = name;
        this.policyClass = policyClass;
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

    public static String getPolicyTypeName(final Class<? extends DiscountPolicy> policyClass) {
        return Arrays.stream(values())
                .filter(type -> type.policyClass.equals(policyClass))
                .map(clazz -> clazz.name)
                .findAny()
                .orElseThrow();
    }
}
