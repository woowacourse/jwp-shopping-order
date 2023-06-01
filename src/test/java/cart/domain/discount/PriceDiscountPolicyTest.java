package cart.domain.discount;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class PriceDiscountPolicyTest {

    private final PriceDiscountPolicy priceDiscountPolicy = new PriceDiscountPolicy();

    @ParameterizedTest(name = "{0}원일 때 {1}원 할인")
    @CsvSource(value = {"100000,10000", "99990,8999", "50000,2500", "30000,900", "9990,0"})
    void calculateDiscountAmount(final int price, final int expected) {
        assertThat(priceDiscountPolicy.calculateDiscountAmount(price)).isEqualTo(expected);
    }

}
