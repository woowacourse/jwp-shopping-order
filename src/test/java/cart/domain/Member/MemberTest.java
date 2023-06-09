package cart.domain.Member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;


class MemberTest {

    @Test
    @DisplayName("회원을 생성할때 이메일과 비밀번호가 포맷에 맞으면 제대로 생성된다.")
    void creatMember(){
        assertDoesNotThrow(() -> Member.from("email@aa.aa", "aaaaaaaaaaaaaaaaa"));
    }

    @ParameterizedTest(name = "{displayName} {index} - email : ''{0}''")
    @ValueSource(strings = {"aaa@", "  ", " aaaaaaaaaaaaaaaaa ", "aaa"})
    @DisplayName("회원을 생성할 때, 이메일이 포맷에 맞지 않으면 예외가 발생한다. ")
    void shouldThrowExceptionWhenEmailIsNotFormat(String email) {
        assertThatThrownBy(()-> Member.from(email, "aa"))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "{displayName} {index} - password : ''{0}''")
    @ValueSource(strings = {"", "  ", " sdfjnsdlfn ", ""})
    @DisplayName("회원을 생성할 때, 비밀번호가 포맷에 맞지 않으면 예외가 발생한다.")
    void shouldThrowExceptionWhenPasswordIsNotFormat(String password) {
        assertThatThrownBy(()-> Member.from("email@aa.aa", password))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}