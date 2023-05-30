package cart.domain.value;

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
}
