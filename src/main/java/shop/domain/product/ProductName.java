package shop.domain.product;

import shop.exception.GlobalException;

public class ProductName {
    private static final int MIN_NAME_LENGTH = 1;
    private static final int MAX_NAME_LENGTH = 20;

    private final String name;

    public ProductName(String name) {
        validate(name);
        this.name = name;
    }

    private void validate(String name) {
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new GlobalException("상품 이름은 1글자 이상, 20글자 이하여야 합니다.");
        }
    }

    public String getName() {
        return name;
    }
}
