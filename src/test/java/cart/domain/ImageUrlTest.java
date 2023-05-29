package cart.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ImageUrlTest {

    @Test
    void 정상적으로_생성된다() {
        //given
        final String imageUrl = "chicken.jpeg";

        //expect
        assertThatNoException().isThrownBy(() -> new ImageUrl(imageUrl));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 이미지_주소가_입력되지_않으면_예외를_던진다(final String imageUrl) {
        //expect
        assertThatThrownBy(() -> new ImageUrl(imageUrl))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미지 주소가 입력되지 않았습니다.");
    }

    @Test
    void 이미지_주소가_2000자를_넘으면_예외를_던진다() {
        //given
        final String imageUrl = "chicken.jpeg".repeat(200);

        //expect
        assertThat(imageUrl.length()).isGreaterThan(2000);
        assertThatThrownBy(() -> new ImageUrl(imageUrl))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미지 주소는 2000자를 넘길 수 없습니다.");
    }
}
