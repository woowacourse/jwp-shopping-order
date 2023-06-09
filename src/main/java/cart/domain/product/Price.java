package cart.domain.product;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Price {

    private static final BigDecimal MIN_PRICE_VALUE = BigDecimal.ZERO;
    private final BigDecimal price;

    public Price(final BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    public static Price minPrice() {
        return new Price(MIN_PRICE_VALUE);
    }

    private void validatePrice(final BigDecimal price) {
        if (MIN_PRICE_VALUE.compareTo(price) > 0) {
            throw new IllegalArgumentException("상품 가격은 " + MIN_PRICE_VALUE + "원 이상이여야 합니다.");
        }
    }

    public Price multiply(final int ratio) {
        return new Price(price.multiply(BigDecimal.valueOf(ratio)));
    }

    public Price add(final Price other) {
        return new Price(this.price.add(other.price));
    }

    public Price multiplyAndRound(final double ratio) {
        return new Price(multiply(ratio).setScale(0, RoundingMode.HALF_UP));
    }

    private BigDecimal multiply(final double ratio) {
        return this.price.multiply(BigDecimal.valueOf(ratio));
    }

    public BigDecimal price() {
        return price;
    }
}
