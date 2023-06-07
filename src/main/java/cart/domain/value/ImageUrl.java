package cart.domain.value;

import cart.exception.value.NullOrBlankException;
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
            throw new NullOrBlankException("이미지");
        }
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
