package cart.domain;

import static cart.domain.fixture.DiscountPolicyFixture.defaultDiscountPolicy;
import static cart.domain.fixture.OrderFixture.orderUnderDiscountThreshold;
import static cart.domain.fixture.OrderFixture.orderWithoutId;

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
        Order order = new Order(null, List.of(
            new OrderItem("치킨", 1, "http://example.com/chicken.jpg", new Money(10_000)),
            new OrderItem("피자", 2, "http://example.com/pizza.jpg", new Money(30_000)),
            new OrderItem("보쌈", 1, "http://example.com/pizza.jpg", new Money(15_000))
        ));
        Money money = defaultDiscountPolicy.calculateDiscountAmount(order);

        //then
        Assertions.assertThat(money).isEqualTo(new Money(5_500));
    }

    @Test
    @DisplayName("Order 객체를 받았을 때 정책이 해당 주문에 적용될 수 있으면 true를 반환한다.")
    void canApply_true() {
        //given
        //when
        boolean actual = defaultDiscountPolicy.canApply(orderWithoutId);
        //then
        Assertions.assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("Order 객체를 받았을 때 정책이 해당 주문에 적용될 수 없으면 false를 반환한다.")
    void canApply_false() {
        //given
        //when
        boolean actual = defaultDiscountPolicy.canApply(orderUnderDiscountThreshold);
        //then
        Assertions.assertThat(actual).isFalse();
    }

}