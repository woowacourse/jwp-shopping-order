package cart.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class OrderTest {

    Member member = new Member(1L, "a@a.com", "1234");
    Product product = new Product("cake", 1000, "image.com");

    @Test
    void order_success() {
        //given
        Order order = Order.of(List.of(new CartItem(1L, 2, product, member)), member, 3000);

        //when
        Order confirmed = order.confirmOrder(Money.from(1500), Money.from(500));

        //then
        Assertions.assertThat(confirmed)
                .usingRecursiveComparison()
                .ignoringFields("state")
                .isEqualTo(order);
    }

}