package shop.domain.member;

import shop.exception.ShoppingException;

public class MemberName {
    private static final int MIN_NAME_LENGTH = 4;
    private static final int MAX_NAME_LENGTH = 10;

    private final String name;

    public MemberName(String name) {
        validate(name);
        this.name = name;
    }

    private void validate(String name) {
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new ShoppingException("사용자 이름은 4글자 이상, 10글자 이하여야 합니다.");
        }
    }

    public String getName() {
        return name;
    }
}
