package cart.domain;

public enum PointDiscountPolicy {

    DEFAULT(0.1d);

    private final double percentCondition;

    PointDiscountPolicy(double percentCondition) {
        this.percentCondition = percentCondition;
    }

    public boolean isUnAvailableUsedPoints(Money totalPrice, Money usedPoints) {
        Money moneyCondition = calculatePointCondition(totalPrice);

        return usedPoints.isGreaterThan(moneyCondition);
    }

    public Money calculatePointCondition(Money totalPrice) {
        return totalPrice.multiply(percentCondition);
    }
}
