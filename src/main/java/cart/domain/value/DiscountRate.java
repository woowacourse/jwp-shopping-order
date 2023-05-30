package cart.domain.value;

public class DiscountRate {

    private final int discountRate;

    public DiscountRate(final int discountRate) {
        validateDiscountRate(discountRate);
        this.discountRate = discountRate;
    }

    private void validateDiscountRate(final int discountRate) {
        if (!(0 <= discountRate && discountRate <= 100)) {
            throw new IllegalArgumentException("할인률은 0과 100 사이여야 한다.");
        }
    }

    public int getDiscountRate() {
        return discountRate;
    }
}
