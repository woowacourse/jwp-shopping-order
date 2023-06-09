package cart.domain.value;

import java.util.Objects;
import org.springframework.util.ObjectUtils;

public class ImageUrl {

    private static final String BLANK = " ";

    private final String value;

    public ImageUrl(final String value) {
        validateBlank(value);
        this.value = value;
    }

    private void validateBlank(final String value) {
        if (ObjectUtils.isEmpty(value) || value.contains(BLANK)) {
            throw new IllegalArgumentException("이미지 URL에 공백은 허용되지 않습니다.");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ImageUrl imageUrl = (ImageUrl) o;
        return Objects.equals(getValue(), imageUrl.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
