package cart.domain.value;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class ImageUrlTest {

    @DisplayName("이미지 URL에 공백이 존재하면 예외가 발생한다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "image.u rl"})
    void validateBlank(String input) {
        // when, then
        assertThatThrownBy(() -> new ImageUrl(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미지 URL에 공백은 허용되지 않습니다.");
    }
}
