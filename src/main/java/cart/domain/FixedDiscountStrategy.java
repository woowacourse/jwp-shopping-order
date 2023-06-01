package cart.domain;

import org.springframework.stereotype.Component;

@Component
public class FixedDiscountStrategy implements DiscountStrategy {
    private static final Price FIRST_STANDARD = new Price(50_000);
    private static final Price SECOND_STANDARD = new Price(100_000);
    private static final Price THIRD_STANDARD = new Price(200_000);
    private static final Price FIRST_DISCOUNT_PRICE = new Price(2_000);
    private static final Price SECOND_DISCOUNT_PRICE = new Price(5_000);
    private static final Price THIRD_DISCOUNT_PRICE = new Price(12_000);

    public Payment calculate(Price price) {
        if (price.lessThan(FIRST_STANDARD)) {
            return new Payment(price);
        }
        if (price.lessThan(SECOND_STANDARD)) {
            return new Payment(price, FIRST_DISCOUNT_PRICE);
        }
        if (price.lessThan(THIRD_STANDARD)) {
            return new Payment(price, SECOND_DISCOUNT_PRICE);
        }
        return new Payment(price, THIRD_DISCOUNT_PRICE);
    }
}
