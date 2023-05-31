package cart.domain.coupon;

import java.util.function.BiFunction;

public enum DiscountType {

    rate((price, amount) -> (int) Math.ceil(price * (amount / 100.0))),
    price((price, amount) -> (int) Math.ceil(amount));

    private BiFunction<Integer, Integer, Integer> expression;

    DiscountType(final BiFunction<Integer, Integer, Integer> expression) {
        this.expression = expression;
    }

    public int calculate(int price, int amount) {
        return expression.apply(price, amount);
    }
}
