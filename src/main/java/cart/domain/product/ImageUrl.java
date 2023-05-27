package cart.domain.product;

import cart.exception.ProductException.ImageEmpty;
import cart.exception.ProductException.ImageLength;
import org.apache.logging.log4j.util.Strings;

class ImageUrl {

    private static final int MAXIMUM_LENGTH = 512;

    private final String value;

    public ImageUrl(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (Strings.isEmpty(value)) {
            throw new ImageEmpty();
        }
        if (value.length() > MAXIMUM_LENGTH) {
            throw new ImageLength(value.length(), MAXIMUM_LENGTH);
        }
    }

    public String getValue() {
        return value;
    }
}
