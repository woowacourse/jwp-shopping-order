package cart.domain;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class DefaultDiscountPolicyTest {

    @Test
    @DisplayName("caculateDiscountAmount()를 호출하면 할인금액을 계산한다.")
    void calculateDiscountAmount() {
        //given
        //when
        DiscountPolicy defaultDiscountPolicy = new DefaultDiscountPolicy(0.1);
        Order order = new Order(null, List.of(
                new OrderItem("치킨", 1, "http://example.com/chicken.jpg", new Money(10_000)),
                new OrderItem("피자", 2, "http://example.com/pizza.jpg", new Money(30_000)),
                new OrderItem("보쌈", 1, "http://example.com/pizza.jpg", new Money(15_000))
        ));
        Money money = defaultDiscountPolicy.calculateDiscountAmount(order);

        //then
        Assertions.assertThat(money).isEqualTo(new Money(5_500));
    }

}