package cart.domain.product;

import cart.exception.ProductException.NameEmpty;
import cart.exception.ProductException.NameLength;
import org.apache.logging.log4j.util.Strings;

class Name {

    private static final int MAXIMUM_LENGTH = 255;

    private final String value;

    public Name(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (Strings.isBlank(value)) {
            throw new NameEmpty();
        }
        if (value.length() > MAXIMUM_LENGTH) {
            throw new NameLength(value.length(), MAXIMUM_LENGTH);
        }
    }

    public String getValue() {
        return value;
    }
}
