package cart.domain.product;

import java.util.Objects;
import java.util.regex.Pattern;

public class ProductImageUrl {

    private static final String regex = "^https?:\\/\\/(?:www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)$";

    private final String imageUrl;

    public ProductImageUrl(final String imageUrl) {
        validate(imageUrl);
        this.imageUrl = imageUrl;
    }

    private void validate(String target) {
        validateBlank(target);
        validateRegex(target);
    }

    private void validateBlank(String target) {
        if (target.isBlank()) {
            throw new IllegalArgumentException("상품 이미지 URL을 입력해주세요.");
        }
    }

    private void validateRegex(String target) {
        if (!Pattern.matches(regex, target)) {
            throw new IllegalArgumentException("올바른 URL 형식으로 입력해주세요.");
        }
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductImageUrl that = (ProductImageUrl) o;
        return Objects.equals(imageUrl, that.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageUrl);
    }

    @Override
    public String toString() {
        return "ProductImageUrl{" +
                "imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
