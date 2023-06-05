package cart.domain;

import java.util.Arrays;

public enum DiscountStep {
    FIRST_STEP(new Price(30_000), new Price(2_000)),
    SECOND_STEP(new Price(50_000), new Price(5_000));

    private static final Price NO_DISCOUNT_AMOUNT = new Price(0);

    private final Price discountBoundaryPrice;
    private final Price discountAmount;

    DiscountStep(final Price discountBoundaryPrice, final Price discountAmount) {
        this.discountBoundaryPrice = discountBoundaryPrice;
        this.discountAmount = discountAmount;
    }

    public static Price findDiscountAmount(final Price price) {
        return Arrays.stream(values())
                .filter(discountStep -> discountStep.isOverBoundaryPrice(price))
                .map(discountStep -> discountStep.discountAmount)
                .reduce(Price::getBigger)
                .orElse(NO_DISCOUNT_AMOUNT);
    }

    private boolean isOverBoundaryPrice(final Price price) {
        return price.isGreaterThanOrEqualTo(discountBoundaryPrice);
    }
}
