package cart.domain.product;

public class Price {

    private static final Long MIN_PRICE_VALUE = 0L;
    private final Long price;

    public Price(final Long price) {
        validatePrice(price);
        this.price = price;
    }

    public static Price minPrice() {
        return new Price(MIN_PRICE_VALUE);
    }

    private void validatePrice(final Long price) {
        if (price < MIN_PRICE_VALUE) {
            throw new IllegalArgumentException("상품 가격은 " + MIN_PRICE_VALUE + "원 이상이여야 합니다.");
        }
    }

    public Price multiply(final int ratio) {
        return new Price(price * ratio);
    }

    public Price add(final Price other) {
        return new Price(this.price + other.price);
    }

    public Price multiplyAndRound(final double ratio) {
        return new Price(Math.round(this.price * ratio));
    }

    public Long price() {
        return price;
    }
}
