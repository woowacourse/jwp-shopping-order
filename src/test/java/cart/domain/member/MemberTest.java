package cart.domain.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.badrequest.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("멤버 이메일은 존재하지 않거나 비어있을 수 없습니다.");
        }

        @ParameterizedTest
        @ValueSource(ints = {256, 1024})
        @DisplayName("멤버 이메일이 최대 길이를 초과하면 예외를 던진다.")
        void emailOverLength(int emailLength) {
            String email = "a".repeat(emailLength);

            assertThatThrownBy(() -> new Member(email, "password1", 10))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("멤버 이메일은 최대 255글자까지 가능합니다. 현재 길이: " + emailLength);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("멤버 비밀번호가 존재하지 않거나, 비어있으면 예외를 던진다.")
        void emptyPassword(String password) {
            assertThatThrownBy(() -> new Member("a@a.com", password, 10))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("멤버 비밀번호는 존재하지 않거나 비어있을 수 없습니다.");
        }

        @ParameterizedTest
        @ValueSource(ints = {256, 1024})
        @DisplayName("멤버 비밀번호가 최대 길이를 초과하면 예외를 던진다.")
        void passwordOverLength(int passwordLength) {
            String password = "a".repeat(passwordLength);

            assertThatThrownBy(() -> new Member("a@a.com", password, 10))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("멤버 비밀번호는 최대 255글자까지 가능합니다. 현재 길이: " + passwordLength);
        }

        @ParameterizedTest
        @ValueSource(ints = {Integer.MIN_VALUE, -1})
        @DisplayName("멤버 포인트가 0보다 작으면 예외를 던진다.")
        void negativePoint(int point) {
            assertThatThrownBy(() -> new Member("a@a.com", "password1", point))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("멤버 포인트는 최소 0원부터 가능합니다. 현재 포인트: " + point);
        }
    }

    @Nested
    @DisplayName("usePoint 메서드는 ")
    class UsePoint {

        @Test
        @DisplayName("사용하는 포인트가 음수인 경우 예외를 던진다.")
        void negativePoint() {
            Member member = new Member("a@a.com", "password1", 1000);

            assertThatThrownBy(() -> member.usePoint(-1))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("감소되는 포인트는 최소 0원부터 가능합니다. 현재 감소 포인트: " + -1);
        }

        @ParameterizedTest
        @ValueSource(ints = {1001, 5000, 10000})
        @DisplayName("사용하는 포인트가 보유한 포인트보다 많은 경우 예외를 던진다.")
        void invalidPoint(int point) {
            Member member = new Member("a@a.com", "password1", 1000);

            assertThatThrownBy(() -> member.usePoint(point))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("멤버 포인트는 최소 0원부터 가능합니다. 현재 포인트: " + (1000 - point));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 500, 1000})
        @DisplayName("사용하는 포인트가 보유한 보인트보다 작거나 같은 경우 포인트를 사용한다.")
        void validPoint(int point) {
            Member member = new Member("a@a.com", "password1", 1000);

            member.usePoint(point);

            assertThat(member.getPoint()).isEqualTo(1000 - point);
        }
    }

    @Nested
    @DisplayName("addPoint 메서드는 ")
    class AddPoint {

        @Test
        @DisplayName("추가된 포인트가 음수인 경우 예외를 던진다.")
        void negativePoint() {
            Member member = new Member("a@a.com", "password1", 1000);

            assertThatThrownBy(() -> member.addPoint(-1))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("증가되는 포인트는 최소 0원부터 가능합니다. 현재 증가 포인트: " + -1);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 500, 1000})
        @DisplayName("포인트를 추가한다.")
        void addPoint(int point) {
            Member member = new Member("a@a.com", "password1", 1000);

            member.addPoint(point);

            assertThat(member.getPoint()).isEqualTo(1000 + point);
        }
    }
}
