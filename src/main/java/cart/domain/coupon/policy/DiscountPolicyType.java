package cart.domain.coupon.policy;

import cart.exception.DiscountPolicyNotFoundException;
import java.util.Arrays;
import java.util.function.Function;

public enum DiscountPolicyType {
    RATE(RateDiscountPolicy::new),
    PRICE(PriceDiscountPolicy::new),
    DELIVERY((value) -> new DeliveryDiscountPolicy()),
    NONE((value) -> new NonDiscountPolicy());

    private final Function<Long, DiscountPolicy> discountPolicyFunction;

    DiscountPolicyType(final Function<Long, DiscountPolicy> discountPolicyFunction) {
        this.discountPolicyFunction = discountPolicyFunction;
    }

    public static DiscountPolicy findDiscountPolicy(final String type, final Long value
    ) {
        if (type == null) {
            return NONE.discountPolicyFunction.apply(0L);
        }
        DiscountPolicyType findType = Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(DiscountPolicyNotFoundException::new);

        return findType.discountPolicyFunction.apply(value);
    }
}
