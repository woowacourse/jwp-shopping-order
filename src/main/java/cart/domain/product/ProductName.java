package cart.domain.product;

import static cart.exception.ErrorCode.PRODUCT_NAME_LENGTH;

import cart.exception.BadRequestException;
import java.util.Objects;

public class ProductName {

    private static final int NAME_MIN_LENGTH = 1, NAME_MAX_LENGTH = 20;

    private final String name;

    private ProductName(final String name) {
        this.name = name;
    }

    static ProductName create(final String name) {
        validateName(name);
        return new ProductName(name);
    }

    private static void validateName(final String name) {
        if (name.length() < NAME_MIN_LENGTH || name.length() > NAME_MAX_LENGTH) {
            throw new BadRequestException(PRODUCT_NAME_LENGTH);
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
        final ProductName that = (ProductName) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getName() {
        return name;
    }
}
