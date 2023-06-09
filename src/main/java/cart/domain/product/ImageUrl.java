package cart.domain.product;

import java.net.URI;
import java.net.URISyntaxException;

public class ImageUrl {

    private final String imageUrl;

    public ImageUrl(final String imageUrl) {
        validateImageUrl(imageUrl);
        this.imageUrl = imageUrl;
    }

    private void validateImageUrl(final String imageUrl) {
        try {
            new URI(imageUrl);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("정확한 URL을 넣어주세요");
        }
    }

    public String imageUrl() {
        return imageUrl;
    }
}
