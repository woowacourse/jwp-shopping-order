package cart.domain.order;

import cart.exception.OrderUnauthorizedException;
import cart.fixture.Fixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @Test
    void getTotal() {
        Order order = new Order(1L, 1L, 3000, List.of(Fixture.주문_제품_치킨, Fixture.주문_제품_피자));
        Assertions.assertThat(order.getTotal()).isEqualTo(113_000);
    }

    @Test
    void checkOwner() {
        Order order = new Order(1L, 1L, 3000, List.of(Fixture.주문_제품_치킨, Fixture.주문_제품_피자));
        assertThatThrownBy(() -> order.checkOwner(2L)).isInstanceOf(OrderUnauthorizedException.class);
    }
}
