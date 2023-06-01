package cart.domain.product;

import cart.exception.badrequest.product.ProductImageException;
import org.apache.logging.log4j.util.Strings;

class ImageUrl {

    private static final int MAXIMUM_LENGTH = 512;

    private final String value;

    public ImageUrl(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (Strings.isEmpty(value)) {
            throw new ProductImageException("상품 이미지 경로는 존재하지 않거나 비어있을 수 없습니다.");
        }
        if (value.length() > MAXIMUM_LENGTH) {
            throw new ProductImageException("상품 이미지 경로는 최대 " + MAXIMUM_LENGTH + "글자까지 가능합니다. 현재 길이: " + value.length());
        }
    }

    public String getValue() {
        return value;
    }
}
