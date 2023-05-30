package cart.domain.value;

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
            throw new IllegalArgumentException("상품 이름은 null과 빈 값이 될 수 없습니다.");
        }
    }

    private void validateNameLength(final String name) {
        if (name.length() > MAX_NAME_SIZE) {
            throw new IllegalArgumentException("상품 이름의 길이는 100보다 크면 안됩니다.");
        }
    }

    public String getName() {
        return name;
    }
}
