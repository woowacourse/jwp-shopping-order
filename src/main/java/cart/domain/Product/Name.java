package cart.domain.Product;

import java.util.Objects;

public class Name {
    private static final int MAX_NAME_LENGTH = 50;
    private final String name;

    public Name(String name) {
        this.name = name;
    }

    public static Name from(String name){
        validateName(name);
        return new Name(name);
    }

    private static void validateName(final String name) {
        validateBlank(name);
        validateLength(name);
    }

    private static void validateBlank(final String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name 은 공백일 수 없습니다.");
        }
    }

    private static void validateLength(final String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("Name 은 50자를 넘을 수 없습니다.");
        }
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name1 = (Name) o;
        return Objects.equals(name, name1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
