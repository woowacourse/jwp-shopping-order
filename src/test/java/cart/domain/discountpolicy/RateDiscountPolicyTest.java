package cart.domain.discountpolicy;

import cart.domain.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RateDiscountPolicyTest {

    private final RateDiscountPolicy rateDiscountPolicy = new RateDiscountPolicy();

    @Test
    @DisplayName("정률 할인을 적용한다.")
    void apply_rate_discount_test() {
        // given
        Money original = new Money(10000);
        double discountRate = 0.9;

        // when
        final Money discountedMoney = rateDiscountPolicy.apply(original, discountRate);

        // then
        assertThat(discountedMoney).isEqualTo(new Money(9000));
    }

}
