package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PaymentTest {

    @Test
    @DisplayName("Order로 Payment를 생성한다.")
    void createFromOrder() {
        //given
        Order order = OrderFixture.order;
        List<DiscountPolicy> discountPolicies = List.of(new DefaultDiscountPolicy(0.1));
        Payment expected = new Payment(Money.from(55000), Money.from(5500), Money.from(3500));

        //when
        Payment actual = Payment.of(order, discountPolicies, new DefaultDeliveryPolicy());
        //then
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

}