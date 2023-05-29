package cart.domain;

import cart.domain.product.Name;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class NameTest {

    @Test
    void 정상적으로_생성된다() {
        //given
        final String name = "chicken";

        //expect
        assertThatNoException().isThrownBy(() -> new Name(name));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 상품_이름을_입력하지_않으면_예외를_던진다(final String name) {
        //expect
        assertThatThrownBy(() -> new Name(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 이름이 입력되지 않았습니다.");
    }

    @Test
    void 상품의_이름이_50자를_넘기면_예외를_던진다() {
        //given
        final String name = "chicken".repeat(8);

        //expect
        assertThatThrownBy(() -> new Name(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 이름은 50자를 넘길 수 없습니다.");
    }
}
