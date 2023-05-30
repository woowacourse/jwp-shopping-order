package coupon.domain.discountpolicy;

import static org.assertj.core.api.Assertions.assertThat;

import coupon.domain.Money;
import coupon.domain.Order;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
class PercentDiscountPolicyTest {

    @ParameterizedTest
    @CsvSource(value = {"10000, 1000", "20000, 2000", "30000, 3000"})
    void 할인_금액을_계산한다(int input, int expected) {
        // given
        PercentDiscountPolicy discountPolicy = new PercentDiscountPolicy(10);

        // when
        Money result = discountPolicy.calculateDiscountAmount(new Order(input));

        // then
        assertThat(result).isEqualTo(new Money(expected));
    }
}
