package cart.domain.product;

import cart.exception.ExceptionType;
import cart.exception.ProductException;
import java.util.Objects;

public class ImageUrl {

    private static final String IMAGE_URL_PREFIX = "http";

    private final String value;

    public ImageUrl(String value) {
        validateImageUrl(value);
        this.value = value;
    }

    public static ImageUrl from(String value) {
        return new ImageUrl(value);
    }

    private void validateImageUrl(String imageUrl) {
        if (!imageUrl.startsWith(IMAGE_URL_PREFIX)) {
            throw new ProductException(ExceptionType.INVALID_PRODUCT_IMAGE_URL);
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
        ImageUrl imageUrl = (ImageUrl) o;
        return Objects.equals(value, imageUrl.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
