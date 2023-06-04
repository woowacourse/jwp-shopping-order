package cart.domain.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static cart.TestFeatures.회원1_주문1_포인트;
import static cart.TestFeatures.회원1_주문4_포인트;
import static org.assertj.core.api.AssertionsForClassTypes.*;

class PointTest {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @DisplayName("expiredAt이 30일 이하로 남은 경우 true를 반환한다")
    @Test
    void isToBeExpired() {
        // given
        LocalDateTime compareDateTime = LocalDateTime.parse("2023-05-15 12:12:12", formatter);
        Point point = 회원1_주문1_포인트;

        // when
        boolean isToBeExpired = point.isToBeExpired(compareDateTime);

        // then
        assertThat(isToBeExpired).isTrue();
    }

    @DisplayName("expiredAt이 30일 초과로 남은 경우 false를 반환한다")
    @Test
    void isNotToBeExpired() {
        // given
        LocalDateTime compareDateTime = LocalDateTime.parse("2023-05-15 12:12:12", formatter);
        Point point = 회원1_주문4_포인트;

        // when
        boolean isToBeExpired = point.isToBeExpired(compareDateTime);

        // then
        assertThat(isToBeExpired).isFalse();
    }
}
