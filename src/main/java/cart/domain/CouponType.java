package cart.domain;

import java.util.Arrays;
import java.util.function.BiFunction;

public enum CouponType {

    FIXED((paymentAmount, discountAmount) -> paymentAmount - discountAmount),
    RATE((paymentAmount, discountAmount) -> (int) (paymentAmount * (1 - discountAmount / 100.0)));

    private final BiFunction<Integer, Integer, Integer> expression;

    CouponType(BiFunction<Integer, Integer, Integer> expression) {
        this.expression = expression;
    }

    public int apply(int paymentAmount, int discountAmount) {
        return expression.apply(paymentAmount, discountAmount);
    }

    public static CouponType from(String typeName) {
        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(typeName))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
