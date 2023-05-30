package cart.domain.product;

import static cart.exception.ErrorCode.PRODUCT_PRICE_RANGE;

import cart.exception.BadRequestException;
import java.util.Objects;

public class ProductPrice {

    private static final int DISCOUNT_RATE_MIN_VALUE = 1, DISCOUNT_RATE_MAX_VALUE = 10_000_000;

    private final int price;

    private ProductPrice(final int price) {
        this.price = price;
    }

    public static ProductPrice create(final int price) {
        validateRange(price);
        return new ProductPrice(price);
    }

    private static void validateRange(final int discountRate) {
        if (discountRate < DISCOUNT_RATE_MIN_VALUE || discountRate > DISCOUNT_RATE_MAX_VALUE) {
            throw new BadRequestException(PRODUCT_PRICE_RANGE);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductPrice that = (ProductPrice) o;
        return price == that.price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }

    public int getPrice() {
        return price;
    }
}
