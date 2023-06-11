package cart.domain;

public enum DiscountPolicy {
    FIXED {
        @Override
        public Money discount(Money money, int value) {
            if (money.isNegativeBySubtracting(value)) {
                return Money.ZERO;
            }
            return money.minus(new Money(value));
        }
    },
    PERCENTAGE {
        @Override
        public Money discount(Money money, int percentage) {
            return money.minus(money.percentageOf(percentage));
        }
    };

    public abstract Money discount(Money money, int amount);
}
