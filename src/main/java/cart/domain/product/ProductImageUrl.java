package cart.domain.product;

import java.util.Objects;

public class ProductImageUrl {

    private final String imageUrl;

    public ProductImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
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
        final ProductImageUrl other = (ProductImageUrl) o;
        return Objects.equals(imageUrl, other.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageUrl);
    }
}
