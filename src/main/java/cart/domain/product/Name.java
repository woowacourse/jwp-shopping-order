package cart.domain.product;

public class Name {

    private static final int MAX_NAME_LENGTH = 100;
    private final String name;

    public Name(final String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(final String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("상품명의 길이는 " + MAX_NAME_LENGTH + "자 이하여야합니다.");
        }
    }

    public String name() {
        return name;
    }
}
