package cart.domain;

import cart.domain.member.Password;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PasswordTest {

    @Test
    void 정상적으로_생성된다() {
        //given
        final String value = "12345abc!!";

        //expect
        assertThatNoException().isThrownBy(() -> new Password(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345a!", "123456789abcdefghijk!"})
    void 유효한_범위를_벗어나면_예외를_던진다(final String value) {
        //expect
        assertThatThrownBy(() -> new Password(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호는 최소 8자 이상, 최대 20자 이하여야 합니다.");
    }

    @Test
    void 숫자를_포함하지_않으면_예외를_던진다() {
        //given
        final String value = "abcdefghij!";

        //expect
        assertThatThrownBy(() -> new Password(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호는 숫자를 포함해야 합니다.");
    }

    @Test
    void 대소문자를_포함하지_않으면_예외를_던진다() {
        //given
        final String value = "123456789!";

        //expect
        assertThatThrownBy(() -> new Password(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호는 대소문자를 포함해야 합니다.");
    }

    @Test
    void 특수문자를_포함하지_않으면_예외를_던진다() {
        //given
        final String value = "12345689a";

        //expect
        assertThatThrownBy(() -> new Password(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호는 특수문자를 포함해야 합니다.");
    }
}
