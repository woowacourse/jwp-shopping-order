package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.sql.Timestamp;
import java.util.List;

import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void construct() {
        final Order result = new Order(1L, new CartItems(
                List.of(
                        new CartItem(1L, null, null, 0),
                        new CartItem(2L, null, null, 0)
                )),
                new OrderPoint(1L, null, null),
                Timestamp.valueOf("2023-11-31 10:00:00")
        );
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(1L),
                () -> assertThat(result.getCartItems().getCartItems().get(0).getId()).isEqualTo(1L),
                () -> assertThat(result.getCartItems().getCartItems().get(1).getId()).isEqualTo(2L),
                () -> assertThat(result.getOrderPoint().getPointId()).isEqualTo(1L),
                () -> assertThat(result.getCreatedAt()).isEqualTo(Timestamp.valueOf("2023-11-31 10:00:00"))
        );
    }

    @Test
    void constructWithNoId() {
        final Order result = new Order(new CartItems(
                List.of(
                        new CartItem(1L, null, null, 0),
                        new CartItem(2L, null, null, 0)
                )),
                new OrderPoint(1L, null, null),
                Timestamp.valueOf("2023-11-31 10:00:00")
        );
        assertAll(
                () -> assertThat(result.getId()).isNull(),
                () -> assertThat(result.getCartItems().getCartItems().get(0).getId()).isEqualTo(1L),
                () -> assertThat(result.getCartItems().getCartItems().get(1).getId()).isEqualTo(2L),
                () -> assertThat(result.getOrderPoint().getPointId()).isEqualTo(1L),
                () -> assertThat(result.getCreatedAt()).isEqualTo(Timestamp.valueOf("2023-11-31 10:00:00"))
        );
    }

    @Test
    void constructWithOther() {
        final Order order = new Order(new CartItems(
                List.of(
                        new CartItem(1L, null, null, 0),
                        new CartItem(2L, null, null, 0)
                )),
                new OrderPoint(1L, null, null),
                Timestamp.valueOf("2023-11-31 10:00:00")
        );
        final Order result = new Order(10L, order);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(10L),
                () -> assertThat(result.getCartItems().getCartItems().get(0).getId()).isEqualTo(1L),
                () -> assertThat(result.getCartItems().getCartItems().get(1).getId()).isEqualTo(2L),
                () -> assertThat(result.getOrderPoint().getPointId()).isEqualTo(1L),
                () -> assertThat(result.getCreatedAt()).isEqualTo(Timestamp.valueOf("2023-11-31 10:00:00"))
        );
    }
}
