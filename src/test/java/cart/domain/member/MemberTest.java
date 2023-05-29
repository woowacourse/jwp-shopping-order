package cart.domain.member;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class MemberTest {

    @Nested
    @DisplayName("멤버 생성 시 ")
    class CreateMember {

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("멤버 이메일이 존재하지 않거나, 비어있으면 예외를 던진다.")
        void emptyEmail(String email) {
            assertThatThrownBy(() -> new Member(email, "password1", 10))
                    .isInstanceOf(MemberException.class)
                    .hasMessage("멤버 이메일은 필수입니다.");
        }

        @ParameterizedTest
        @ValueSource(ints = {256, 1024})
        @DisplayName("멤버 이메일이 최대 길이를 초과하면 예외를 던진다.")
        void emailOverLength(int emailLength) {
            String email = "a".repeat(emailLength);

            assertThatThrownBy(() -> new Member(email, "password1", 10))
                    .isInstanceOf(MemberException.class)
                    .hasMessage("멤버 이메일은 최대 255글자까지 가능합니다. 현재 길이: " + emailLength);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("멤버 비밀번호가 존재하지 않거나, 비어있으면 예외를 던진다.")
        void emptyPassword(String password) {
            assertThatThrownBy(() -> new Member("a@a.com", password, 10))
                    .isInstanceOf(MemberException.class)
                    .hasMessage("멤버 비밀번호는 필수입니다.");
        }

        @ParameterizedTest
        @ValueSource(ints = {256, 1024})
        @DisplayName("멤버 비밀번호가 최대 길이를 초과하면 예외를 던진다.")
        void passwordOverLength(int passwordLength) {
            String password = "a".repeat(passwordLength);

            assertThatThrownBy(() -> new Member("a@a.com", password, 10))
                    .isInstanceOf(MemberException.class)
                    .hasMessage("멤버 비밀번호는 최대 255글자까지 가능합니다. 현재 길이: " + passwordLength);
        }

        @ParameterizedTest
        @ValueSource(ints = {Integer.MIN_VALUE, -1})
        @DisplayName("멤버 포인트가 0보다 작으면 예외를 던진다.")
        void negativePoint(int point) {
            assertThatThrownBy(() -> new Member("a@a.com", "password1", point))
                    .isInstanceOf(MemberException.class)
                    .hasMessage("멤버의 포인트는 0보다 작을 수 없습니다.");
        }
    }
}
