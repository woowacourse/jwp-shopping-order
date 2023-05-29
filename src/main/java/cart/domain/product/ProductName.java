package cart.domain.product;

import java.util.regex.Pattern;

public class ProductName {

    private static final String regex = "^[ㄱ-ㅎ가-힣a-zA-Z]*$";

    private final String name;

    public ProductName(final String name) {
        validate(name);
        this.name = name;
    }

    private void validate(String target) {
        validateBlank(target);
        validateRegex(target);
    }

    private void validateBlank(String target) {
        if (target.isBlank()) {
            throw new IllegalArgumentException("상품 이름을 입력해주세요.");
        }
    }

    private void validateRegex(String target) {
        if (!Pattern.matches(regex, target)) {
            throw new IllegalArgumentException("상품 이름은 한글 또는 영어여야합니다.");
        }
    }

    public String getName() {
        return name;
    }
}
