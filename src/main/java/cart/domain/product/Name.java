package cart.domain.product;

import cart.exception.badrequest.product.ProductNameException;
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
            throw new ProductNameException("상품 이름은 존재하지 않거나 비어있을 수 없습니다.");
        }
        if (value.length() > MAXIMUM_LENGTH) {
            throw new ProductNameException("상품 이름은 최대 " + MAXIMUM_LENGTH + "글자까지 가능합니다. 현재 길이: " + value.length());
        }
    }

    public String getValue() {
        return value;
    }
}
