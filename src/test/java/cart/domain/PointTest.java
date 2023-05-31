package cart.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("포인트 관련 테스트")
class PointTest {

    @DisplayName("포인트를 생성합니다.")
    @ValueSource(ints = {1, 2, 1000, 10000})
    @ParameterizedTest
    void createPoint(int value) {
        //given
        Member member = null;
        LocalDateTime now = LocalDateTime.now();

        //when
        Point point = new Point(value, value, member, now.plusDays(1), now);

        // then
        assertEquals(value, point.getEarnedPoint());
        assertEquals(value, point.getLeftPoint());
        assertEquals(now, point.getCreatedAt());
        assertEquals(now.plusDays(1), point.getExpiredAt());
    }

    @DisplayName("포인트를 음수로 발급할 수 없습니다.")
    @ValueSource(ints = {-1, -1000, -100000})
    @ParameterizedTest
    void doseNotCreateNegativePoint(int value) {
        //given
        Member member = null;
        LocalDateTime now = LocalDateTime.now();

        //when, then
        assertThatThrownBy(() -> new Point(value, value, member, now.plusDays(1), now))
                .isInstanceOf(IllegalArgumentException.class)
                .describedAs("포인트는 음수가 될 수 없습니다.");

    }

    @DisplayName("잔여 금액이 발급 금액을 초과할 수 없습니다.")
    @CsvSource(value = {"100:1000", "10000:50000"}, delimiter = ':')
    @ParameterizedTest
    void doseNotCreateLeftOverEarned(int earned, int left) {
        //given
        Member member = null;
        LocalDateTime now = LocalDateTime.now();

        //when, then
        assertThatThrownBy(() -> new Point(earned, left, member, now.plusDays(1), now))
                .isInstanceOf(IllegalArgumentException.class)
                .describedAs("잔여 포인트는 발급 포인트보다 많을 수 없습니다.");

    }

    @DisplayName("유효시각이 만료시각 이전이어야 합니다.")
    @Test
    void doseNotCreateExpiredAfterCreate() {
        //given
        Member member = null;
        LocalDateTime now = LocalDateTime.now();

        //when, then
        assertThatThrownBy(() -> new Point(0, 0, member, now.minusDays(10), now))
                .isInstanceOf(IllegalArgumentException.class)
                .describedAs("유효시각이 만료시각 이전이어야 합니다.");

    }
}
