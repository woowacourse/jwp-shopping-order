package cart.domain.value;

import cart.exception.value.NullOrBlankException;
import cart.exception.value.name.NameLengthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class NameTest {

    @ParameterizedTest(name = "{displayName}")
    @NullSource
    @ValueSource(strings = {" ", ""})
    @DisplayName("상품 이름({0})이 올바르지 않으면 에러를 발생한다.")
    void check_name(String name) {
        // when + then
        assertThatThrownBy(() -> new Name(name))
                .isInstanceOf(NullOrBlankException.class);
    }

    @Test
    @DisplayName("상품 이름의 길이가 100보다 크면 에러를 발생한다.")
    void check_name_size() {
        // given
        String wrongName = "가".repeat(101);

        // when + then
        assertThatThrownBy(() -> new Name(wrongName))
                .isInstanceOf(NameLengthException.class);
    }
}
