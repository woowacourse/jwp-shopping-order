package cart.domain;

import java.util.Arrays;

public enum AmountDiscountRange {
    FIFTH(50_000, 5_000),
    THIRD(30_000, 2_000),
    ;

    private final int lowerBoundPrice;
    private final int discountAmount;

    AmountDiscountRange(final int lowerBoundPrice, final int discountAmount) {
        this.lowerBoundPrice = lowerBoundPrice;
        this.discountAmount = discountAmount;
    }

    public static int findDiscountAmount(final int totalPrice) {
        return Arrays.stream(AmountDiscountRange.values())
                .filter(amountDiscountRange -> amountDiscountRange.checkDiscountablePrice(totalPrice))
                .mapToInt(amountDiscountRange -> amountDiscountRange.discountAmount)
                .max().orElse(0);
    }

    private boolean checkDiscountablePrice(final int totalPrice) {
        return lowerBoundPrice <= totalPrice;
    }
}
