package cart.domain;

public class DefaultDiscountPolicy implements DiscountPolicy {

    private final double discountRate;

    public DefaultDiscountPolicy(double discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public Money calculateDiscountAmount(Order order) {
        return order.calculateOriginalTotalPrice()
                .multiply(this.discountRate);
    }
}
