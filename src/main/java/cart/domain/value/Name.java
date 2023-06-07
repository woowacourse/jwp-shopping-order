package cart.domain.value;

import cart.exception.value.NullOrBlankException;
import cart.exception.value.name.NameLengthException;
import org.springframework.util.ObjectUtils;

public class Name {

    private static final int MAX_NAME_SIZE = 100;

    private final String name;

    public Name(final String name) {
        validateName(name);
        validateNameLength(name);
        this.name = name;
    }

    private void validateName(final String name) {
        if (ObjectUtils.isEmpty(name) || name.isBlank()) {
            throw new NullOrBlankException("상품 이름");
        }
    }

    private void validateNameLength(final String name) {
        if (name.length() > MAX_NAME_SIZE) {
            throw new NameLengthException();
        }
    }

    public String getName() {
        return name;
    }
}
