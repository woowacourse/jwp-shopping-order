package cart.domain;

import cart.domain.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MemberTest {

    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member(1L, "a@a.com", "1234", 100);
    }

    @DisplayName("포인트를 사용한다.")
    @Nested
    class CheckUsePoint {
        @DisplayName("보유한 포인트가 사용 포인트보다 클 경우 포인트를 차감한다.")
        @ParameterizedTest
        @CsvSource(value = {"0:100", "1:99", "50:50", "100:0"}, delimiter = ':')
        void usePoint(final int usingPoint, final int remainPoint) {
            member.usePoint(usingPoint);

            assertThat(member.getPoint()).isEqualTo(remainPoint);
        }

        @DisplayName("보유한 포인트가 사용 포인트보다 작을 경우 Exception이 발생한다.")
        @ParameterizedTest
        @ValueSource(ints = {101, 105, 1_000})
        void useOverPoint(final int usingPoint) {
            assertThrows(IllegalArgumentException.class, () -> member.usePoint(usingPoint));
        }

        @DisplayName("사용하려는 포인트가 0보다 작을 경우 Exception이 발생한다.")
        @ParameterizedTest
        @ValueSource(ints = {-1, -2, -100})
        void useUnderPoint(final int usingPoint) {
            assertThrows(IllegalArgumentException.class, () -> member.usePoint(usingPoint));
        }
    }

    @DisplayName("포인트를 적립한다.")
    @Nested
    class CheckSavePoint {
        @DisplayName("포인트를 적립한다.")
        @ParameterizedTest
        @ValueSource(ints = {0, 1, 100})
        void savePoint(final int savedPoint) {
            final int expectPoint = member.getPoint() + savedPoint;
            member.savePoint(savedPoint);

            assertThat(member.getPoint()).isEqualTo(expectPoint);
        }

        @DisplayName("적립하려는 포인트가 음수인 경우 Exception이 발생한다.")
        @ParameterizedTest
        @ValueSource(ints = {-1, -2, -3})
        void negativeSavePoint(final int savingPoint) {
            assertThrows(IllegalArgumentException.class, () -> member.savePoint(savingPoint));
        }
    }
}
