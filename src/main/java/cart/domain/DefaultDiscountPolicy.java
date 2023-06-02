package cart.domain;

import java.util.Objects;

public class DefaultDiscountPolicy implements DiscountPolicy {
    private final Long id;
    private final String name;
    private final Money threshold;
    private final double discountRate;

    public DefaultDiscountPolicy(final long id, final String name, final Money threshold, final double discountRate) {
        this.id = id;
        this.name = name;
        this.threshold = threshold;
        this.discountRate = discountRate;
    }

    public DefaultDiscountPolicy(final String name, final Money threshold, final double discountRate) {
        this.id = null;
        this.name = name;
        this.threshold = threshold;
        this.discountRate = discountRate;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public Money calculateDiscountAmount(final Order order) {
        return order.calculateOriginalTotalPrice()
                .multiply(this.discountRate);
    }

    @Override
    public boolean canApply(final Order order) {
        final Money totalPrice = order.calculateOriginalTotalPrice();
        return totalPrice.isGreaterThanOrEqual(this.threshold);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final DefaultDiscountPolicy that = (DefaultDiscountPolicy) o;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
