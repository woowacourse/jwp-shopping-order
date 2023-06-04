package cart.domain.point;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.Timestamp;
import java.util.Calendar;

import static cart.fixture.MemberFixture.하디;
import static org.assertj.core.api.Assertions.assertThat;

public class RatePolicyTest {

    private RatePointPolicy pointPolicy = new RatePointPolicy();

    @ParameterizedTest
    @CsvSource(value = {"50, 3", "100, 5", "225, 12", "10000, 500", "9, 1", "10, 1", "0, 0"}, delimiter = ',')
    void 포인트_적립률_5퍼센트_올림을_적용하여_적립될_포인트를_계산한다(long price, long expectedPoint) {
        // given, when
        long actualPoint = pointPolicy.calculateEarnedPoint(하디, price);

        // then
        assertThat(actualPoint).isEqualTo(expectedPoint);
    }

    @Test
    void 포인트_기간_정책에_따라_만료일을_계산한다() {
        // given
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(createdAt);
        calendar.add(Calendar.MONTH, pointPolicy.DURATION_MONTHS);
        Timestamp expected = new Timestamp(calendar.getTimeInMillis());

        // when
        Timestamp actual = pointPolicy.calculateExpiredAt(createdAt);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
