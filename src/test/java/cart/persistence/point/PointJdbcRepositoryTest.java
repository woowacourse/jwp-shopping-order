package cart.persistence.point;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import cart.application.repository.PointRepository;
import cart.domain.Point;
import cart.domain.PointHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@JdbcTest
@Sql(value = "classpath:reset.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class PointJdbcRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private PointRepository pointRepository;

    @BeforeEach
    void setup() {
        pointRepository = new PointJdbcRepository(jdbcTemplate);
    }

    @ParameterizedTest
    @CsvSource({"5, 0, 1000, 1000", "5, 100, 1000, 900"})
    @DisplayName("포인트 사용 이력 추가")
    void createPointHistory(final Long orderId, final int usedPoint, final int earnedPoint, final int nowPoint) {
        // given
        Point beforePoint = pointRepository.findPointByMemberId(5L);

        // when
        pointRepository.createPointHistory(orderId, new PointHistory(orderId, usedPoint, earnedPoint));
        Point afterPoint = pointRepository.findPointByMemberId(orderId);

        // then
        assertSoftly(softly -> {
            softly.assertThat(afterPoint.getPointHistories().size() - beforePoint.getPointHistories().size()).isOne();
            softly.assertThat(afterPoint.calculateTotalPoint()).isEqualTo(beforePoint.calculateTotalPoint() + nowPoint);
        });
    }

}
