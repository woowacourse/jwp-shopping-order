package cart.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.sql.Timestamp;

import org.junit.jupiter.api.Test;

class OrdersEntityTest {

    @Test
    void construct() {
        final OrdersEntity result = new OrdersEntity(
                1L, 2L, 3L, 100, 200,
                Timestamp.valueOf("2023-05-31 10:00:00")
        );
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(1L),
                () -> assertThat(result.getMemberId()).isEqualTo(2L),
                () -> assertThat(result.getPointId()).isEqualTo(3L),
                () -> assertThat(result.getEarnedPoint()).isEqualTo(100),
                () -> assertThat(result.getUsedPoint()).isEqualTo(200),
                () -> assertThat(result.getCreatedAt()).isEqualTo(Timestamp.valueOf("2023-05-31 10:00:00"))
        );
    }

    @Test
    void constructWithNoId() {
        final OrdersEntity result = new OrdersEntity(
                2L, 3L, 100, 200,
                Timestamp.valueOf("2023-05-31 10:00:00")
        );
        assertAll(
                () -> assertThat(result.getId()).isNull(),
                () -> assertThat(result.getMemberId()).isEqualTo(2L),
                () -> assertThat(result.getPointId()).isEqualTo(3L),
                () -> assertThat(result.getEarnedPoint()).isEqualTo(100),
                () -> assertThat(result.getUsedPoint()).isEqualTo(200),
                () -> assertThat(result.getCreatedAt()).isEqualTo(Timestamp.valueOf("2023-05-31 10:00:00"))
        );
    }

    @Test
    void constructWithOther() {
        final OrdersEntity ordersEntity = new OrdersEntity(
                1L, 2L, 3L, 100, 200,
                Timestamp.valueOf("2023-05-31 10:00:00")
        );
        final OrdersEntity result = new OrdersEntity(10L, ordersEntity);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(10L),
                () -> assertThat(result.getMemberId()).isEqualTo(2L),
                () -> assertThat(result.getPointId()).isEqualTo(3L),
                () -> assertThat(result.getEarnedPoint()).isEqualTo(100),
                () -> assertThat(result.getUsedPoint()).isEqualTo(200),
                () -> assertThat(result.getCreatedAt()).isEqualTo(Timestamp.valueOf("2023-05-31 10:00:00"))
        );
    }
}
