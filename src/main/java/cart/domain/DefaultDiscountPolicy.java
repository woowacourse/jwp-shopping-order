package cart.domain;

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
    public long getId() {
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
}
