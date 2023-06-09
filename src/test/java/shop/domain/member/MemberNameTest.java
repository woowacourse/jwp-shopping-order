package shop.domain.member;

import shop.exception.ShoppingException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MemberNameTest {

    @ParameterizedTest(name = "사용자의 이름은 4글자 이상, 10글자 이하여야 한다.")
    @ValueSource(strings = {"1", "123", "", "이것은 열한글자입니다"})
    void createMemberNameTest1(String name) {
        assertThatThrownBy(() -> new MemberName(name))
                .isInstanceOf(ShoppingException.class);
    }

    @ParameterizedTest(name = "사용자의 이름은 4글자 이상, 10글자 이하여야 한다.")
    @ValueSource(strings = {"네글자다", "이것은 열글자입니다"})
    void createMemberNameTest2(String name) {
        assertDoesNotThrow(() -> new MemberName(name));
    }

}
