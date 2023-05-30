package cart.domain.value;

import org.springframework.util.ObjectUtils;

public class ImageUrl {

    private static final String SPACE = " ";

    private final String imageUrl;

    public ImageUrl(final String imageUrl) {
        validateImage(imageUrl);
        this.imageUrl = imageUrl;
    }

    private void validateImage(final String imageUrl) {
        if (ObjectUtils.isEmpty(imageUrl) || imageUrl.contains(SPACE)) {
            throw new IllegalArgumentException("이미지는 null이나 빈 값이 될 수 없습니다.");
        }
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
