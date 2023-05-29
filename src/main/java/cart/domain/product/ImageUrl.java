package cart.domain.product;

import java.util.Objects;

public class ImageUrl {

    private static final int MAXIMUM_IMAGE_LENGTH = 2_000;

    private final String imageUrl;

    public ImageUrl(final String imageUrl) {
        validate(imageUrl);
        this.imageUrl = imageUrl;
    }

    private void validate(final String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("이미지 주소가 입력되지 않았습니다.");
        }
        if (imageUrl.length() > MAXIMUM_IMAGE_LENGTH) {
            throw new IllegalArgumentException("이미지 주소는 " + MAXIMUM_IMAGE_LENGTH + "자를 넘길 수 없습니다.");
        }
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ImageUrl imageUrl1 = (ImageUrl) o;
        return Objects.equals(imageUrl, imageUrl1.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageUrl);
    }
}
