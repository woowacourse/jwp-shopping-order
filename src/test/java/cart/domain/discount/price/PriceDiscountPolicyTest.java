package cart.domain.discount.price;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import cart.domain.Member;

class PriceDiscountPolicyTest {

    PriceDiscountPolicy priceDiscountPolicy = new PriceDiscountPolicy();

    @ParameterizedTest(name = "금액에 따른 할인 금액을 계산한다.")
    @MethodSource("providePriceAndExpected")
    void calculateDiscountPrice(Integer price, Integer expected) {
        //given
        final Member member = new Member(1L, "a@a.com", "1234", 1);

        //when
        final int result = priceDiscountPolicy.calculateDiscountPrice(price, member);

        //then
        assertThat(result).isEqualTo(expected);
    }

    public static Stream<Arguments> providePriceAndExpected() {
        return Stream.of(
                Arguments.of(9000, 90),
                Arguments.of(19000, 380),
                Arguments.of(29000, 870),
                Arguments.of(110000, 10000)
        );
    }
}
