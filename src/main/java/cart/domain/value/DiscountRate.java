package cart.domain.value;

import cart.exception.value.discountrate.InvalidDiscountRateException;

public class DiscountRate {

    private final int discountRate;

    public DiscountRate(final int discountRate) {
        validateDiscountRate(discountRate);
        this.discountRate = discountRate;
    }

    private void validateDiscountRate(final int discountRate) {
        if (!(0 <= discountRate && discountRate <= 100)) {
            throw new InvalidDiscountRateException();
        }
    }

    public double getDiscountedPercent() {
        return 1 - (double) discountRate / 100;
    }

    public boolean isDiscount() {
        return discountRate > 0;
    }

    public int getDiscountRate() {
        return discountRate;
    }
}
