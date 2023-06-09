package cart.domain.coupon;

import org.springframework.util.ObjectUtils;

import java.util.Objects;

public class Name {

    private final String name;

    private Name(final String name) {
        if (ObjectUtils.isEmpty(name)) {
            throw new IllegalStateException("이름은 빈 칸일 수 없습니다.");
        }

        this.name = name;
    }

    public static Name from(final String name) {
        return new Name(name);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Name name1 = (Name) o;
        return Objects.equals(name, name1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getName() {
        return name;
    }
}
