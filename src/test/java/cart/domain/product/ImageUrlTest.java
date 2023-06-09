package cart.domain.product;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class ImageUrlTest {

    @Test
    void 정확한_이미지_경로가_아니면_예외를_던진다() {
        // given
        final String wrongUrl = "정확하지않은 URL 임";

        // expect
        assertThatThrownBy(() -> new ImageUrl(wrongUrl))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("정확한 URL을 넣어주세요");
    }

    @Test
    void 정확한_이미지_경로이면_예외가_발생하지_않는다() {
        // given
        final String correctUrl = "https://www.rd.com/wp-content/uploads/2021/01/GettyImages-1175550351.jpg";

        // expect
        assertDoesNotThrow(() -> new ImageUrl(correctUrl));
    }
}
