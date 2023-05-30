package cart.domain.value;

import org.springframework.util.ObjectUtils;

public class Name {

    private final String value;

    public Name(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        validateBlank(value);
        validateRange(value);
    }

    private void validateBlank(final String value) {
        if (ObjectUtils.isEmpty(value)) {
            throw new IllegalArgumentException("비어있는 상품명은 허용되지 않습니다.");
        }
    }

    private void validateRange(final String value) {
        if (value.length() > 100) {
            throw new IllegalArgumentException("상품명은 100자를 초과할 수 없습니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
