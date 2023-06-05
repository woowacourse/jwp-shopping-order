package cart.domain;

public class DiscountPriceCalculator {

    public Price calculate(final Price price) {
        return DiscountStep.findDiscountAmount(price);
    }
}
