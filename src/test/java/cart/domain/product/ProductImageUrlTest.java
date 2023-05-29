package cart.domain.product;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProductImageUrlTest {

    @ParameterizedTest
    @ValueSource(strings = {" ", ""})
    @DisplayName("상품 이미지 URL이 빈 값이면 예외가 발생한다.")
    void throws_when_image_url_blank(String imageUrl) {
        // when, then
        assertThatThrownBy(() -> new ProductImageUrl(imageUrl))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 이미지 URL을 입력해주세요.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"aaa://", "shop.com"})
    @DisplayName("상품 이미지 URL이 올바른 형식이 아니면 예외가 발생한다.")
    void throws_when_image_url_wrong_pattern(String imageUrl) {
        // when, then
        assertThatThrownBy(() -> new ProductImageUrl(imageUrl))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("올바른 URL 형식으로 입력해주세요.");
    }

}
