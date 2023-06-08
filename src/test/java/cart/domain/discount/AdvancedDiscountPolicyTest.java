package cart.domain.discount;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AdvancedDiscountPolicyTest {

    private DiscountPolicy discountPolicy = new AdvancedDiscountPolicy();

    @ParameterizedTest(name = "상품의 가격이 {0}원 일 때 할인 가격은 {1}원 이다.")
    @CsvSource(value = {"99000:0", "100000:10000"}, delimiter = ':')
    void calculate(final Long productPrice, final Long expectDiscountPrice) {
        //given
        //when
        final Long discountPrice = discountPolicy.calculate(productPrice);

        //then
        assertThat(discountPrice).isEqualTo(expectDiscountPrice);
    }
}
