package cart.domain.product;

import cart.exception.ExceptionType;
import cart.exception.ProductException;
import java.util.Objects;

public class ProductName {

    private final String value;

    public ProductName(String value) {
        validateName(value);
        this.value = value;
    }

    public static ProductName from(String value) {
        return new ProductName(value);
    }

    private void validateName(String name) {
        if (name.isBlank() || name.length() <= 1) {
            throw new ProductException(ExceptionType.INVALID_PRODUCT_NAME);
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductName that = (ProductName) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
