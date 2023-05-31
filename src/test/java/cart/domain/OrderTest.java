package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {
    @Test
    @DisplayName("calculateOriginalTotalPrice() 호출 시 주문 금액을 반환한다.")
    void calculateOriginalTotalPrice() {
        //given
        Member member = new Member(null, "hi@hi.com", "password");
        Order order = new Order(member, List.of(
                new OrderItem("치킨", 1, "http://example.com/chicken.jpg", new Money(10_000)),
                new OrderItem("피자", 2, "http://example.com/pizza.jpg", new Money(30_000)),
                new OrderItem("보쌈", 1, "http://example.com/pizza.jpg", new Money(15_000))
        ));
        //when
        Money totalPrice = order.calculateOriginalTotalPrice();

        //then
        assertThat(totalPrice).isEqualTo(new Money(55_000));
    }
}