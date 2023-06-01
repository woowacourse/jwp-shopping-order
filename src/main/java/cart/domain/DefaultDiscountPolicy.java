package cart.domain;

public class DefaultDiscountPolicy implements DiscountPolicy {

    private final String name;
    private final Money threshold;
    private final double discountRate;

    public DefaultDiscountPolicy(String name, Money threshold, double discountRate) {
        this.name = name;
        this.threshold = threshold;
        this.discountRate = discountRate;
    }


    @Override
    public Money calculateDiscountAmount(Order order) {
        return order.calculateOriginalTotalPrice()
                .multiply(this.discountRate);
    }

    @Override
    public boolean canApply(Order order) {
        Money totalPrice = order.calculateOriginalTotalPrice();
        return totalPrice.isGreaterThanOrEqual(threshold);
    }

    @Override
    public String getName() {
        return name;
    }
}
