package cart.domain.value;

import cart.exception.value.NullOrBlankException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ImageUrlTest {

    @ParameterizedTest(name = "{displayName}")
    @NullSource
    @ValueSource(strings = {" ", "", "image url"})
    @DisplayName("올바르지 않은 이미지 url({0})이면 에러를 발생한다.")
    void check_image_url(String imageUrl) {
        // when + then
        assertThatThrownBy(() -> new ImageUrl(imageUrl))
                .isInstanceOf(NullOrBlankException.class);
    }
}
