package cart.domain;

public class DiscountPriceCalculator {

    private static final Price FIRST_STEP_DISCOUNT_BOUNDARY_PRICE = new Price(30000);
    private static final Price FIRST_STEP_DISCOUNT_AMOUNT = new Price(2000);
    private static final Price SECOND_STEP_DISCOUNT_BOUNDARY_PRICE = new Price(50000);
    private static final Price SECOND_STEP_DISCOUNT_AMOUNT = new Price(5000);

    public Price calculate(final Price price) {
        return null;
    }
}
