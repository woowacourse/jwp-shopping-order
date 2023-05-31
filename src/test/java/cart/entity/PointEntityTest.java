package cart.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.sql.Timestamp;

import org.junit.jupiter.api.Test;

class PointEntityTest {

    @Test
    void constructWithOther() {
        final PointEntity pointEntity = new PointEntity(100, 200, 2L,
                Timestamp.valueOf("2023-12-31 10:00:00"), Timestamp.valueOf("2023-05-31 10:00:00")
        );
        final PointEntity result = new PointEntity(10L, pointEntity);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(10L),
                () -> assertThat(result.getEarnedPoint()).isEqualTo(100),
                () -> assertThat(result.getLeftPoint()).isEqualTo(200),
                () -> assertThat(result.getMemberId()).isEqualTo(2L),
                () -> assertThat(result.getExpiredAt()).isEqualTo(Timestamp.valueOf("2023-12-31 10:00:00")),
                () -> assertThat(result.getCreatedAt()).isEqualTo(Timestamp.valueOf("2023-05-31 10:00:00"))
        );
    }

    @Test
    void constructWithNoId() {
        final PointEntity result = new PointEntity(100, 200, 2L,
                Timestamp.valueOf("2023-12-31 10:00:00"), Timestamp.valueOf("2023-05-31 10:00:00")
        );
        assertAll(
                () -> assertThat(result.getId()).isNull(),
                () -> assertThat(result.getEarnedPoint()).isEqualTo(100),
                () -> assertThat(result.getLeftPoint()).isEqualTo(200),
                () -> assertThat(result.getMemberId()).isEqualTo(2L),
                () -> assertThat(result.getExpiredAt()).isEqualTo(Timestamp.valueOf("2023-12-31 10:00:00")),
                () -> assertThat(result.getCreatedAt()).isEqualTo(Timestamp.valueOf("2023-05-31 10:00:00"))
        );
    }

    @Test
    void construct() {
        final PointEntity result = new PointEntity(1L, 100, 200, 2L,
                Timestamp.valueOf("2023-12-31 10:00:00"), Timestamp.valueOf("2023-05-31 10:00:00")
        );
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(1L),
                () -> assertThat(result.getEarnedPoint()).isEqualTo(100),
                () -> assertThat(result.getLeftPoint()).isEqualTo(200),
                () -> assertThat(result.getMemberId()).isEqualTo(2L),
                () -> assertThat(result.getExpiredAt()).isEqualTo(Timestamp.valueOf("2023-12-31 10:00:00")),
                () -> assertThat(result.getCreatedAt()).isEqualTo(Timestamp.valueOf("2023-05-31 10:00:00"))
        );
    }
}
