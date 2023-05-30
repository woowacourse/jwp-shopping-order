package cart.domain.coupon;

import org.springframework.util.ObjectUtils;

import java.util.Objects;

public class Description {

    private final String description;

    private Description(final String description) {
        validate(description);
        this.description = description;
    }

    public static Description from(final String description) {
        return new Description(description);
    }

    private void validate(final String description) {
        if (ObjectUtils.isEmpty(description)) {
            throw new IllegalStateException("설명은 빈 칸일 수 없습니다.");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Description that = (Description) o;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }

    public String getDescription() {
        return description;
    }
}
