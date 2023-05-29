package cart.domain;

import cart.domain.member.Email;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class EmailTest {

    @Test
    void 정상적으로_생성된다() {
        //given
        final String value = "huchu@woowahan.com";

        //expect
        assertThatNoException().isThrownBy(() -> new Email(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"@", "huchu@", "@woowahan", "@woowahan.com", "huchu@woowahan", "huchu@woowahan."})
    void 형식에_어긋나면_예외를_던진다(final String value) {
        //expect
        assertThatThrownBy(() -> new Email(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("형식에 맞지 않는 이메일입니다.");
    }
}
