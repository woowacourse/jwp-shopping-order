package cart.domain.order;

import cart.fixture.Fixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class OrderItemTest {

    @Test
    void getTotalRate() {
        final OrderItem orderItem = new OrderItem(Fixture.치킨, 10, List.of(Fixture.멤버_쿠폰));

        Assertions.assertThat(orderItem.getTotal()).isEqualTo(90_000);
    }

    @Test
    void getTotalPrice() {
        final OrderItem orderItem = new OrderItem(Fixture.치킨, 10, List.of(Fixture.멤버_쿠폰2));

        Assertions.assertThat(orderItem.getTotal()).isEqualTo(98_000);
    }
}
