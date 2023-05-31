package cart.domain.coupon.policy;

import cart.exception.DiscountPolicyNotFoundException;
import java.util.Arrays;
import java.util.function.Function;

public enum DiscountPolicyType {
    RATE(RateDiscountPolicy::new),
    PRICE(PriceDiscountPolicy::new),
    DELIVERY((value) -> new DeliveryDiscountPolicy()),
    NONE((value) -> new NonDiscountPolicy());

    private final Function<Long, DiscountPolicy> discountPolicyTriFunction;

    DiscountPolicyType(final Function<Long, DiscountPolicy> discountPolicyFunction) {
        this.discountPolicyTriFunction = discountPolicyFunction;
    }

    public static DiscountPolicy findDiscountPolicy(final String type, final Long value
    ) {
        DiscountPolicyType findType = Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(DiscountPolicyNotFoundException::new);

        return findType.discountPolicyTriFunction.apply(value);
    }
}
