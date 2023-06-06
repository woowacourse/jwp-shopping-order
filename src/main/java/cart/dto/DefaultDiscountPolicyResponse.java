package cart.dto;

import cart.domain.DiscountPolicy;

public class DefaultDiscountPolicyResponse {
    private final Long id;
    private final String name;
    private final double discountRate;
    private final int threshold;

    public DefaultDiscountPolicyResponse(final Long id, final String name, final double discountRate, final int threshold) {
        this.id = id;
        this.name = name;
        this.discountRate = discountRate;
        this.threshold = threshold;
    }

    public static DefaultDiscountPolicyResponse from(final DiscountPolicy defaultDiscountPolicy) {
        return new DefaultDiscountPolicyResponse(defaultDiscountPolicy.getId(), defaultDiscountPolicy.getName(), defaultDiscountPolicy.getDiscountRate(), defaultDiscountPolicy.getThreshold().getValue());
    }

    public String getName() {
        return this.name;
    }

    public Long getId() {
        return this.id;
    }

    public double getDiscountRate() {
        return this.discountRate;
    }

    public int getThreshold() {
        return this.threshold;
    }
}
