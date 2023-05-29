package cart.domain.discount.price;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class BasicPriceDiscountPolicyTest {

    BasicPriceDiscountPolicy basicPriceDiscountPolicy = new BasicPriceDiscountPolicy();

    @ParameterizedTest(name = "금액에 따른 할인 금액을 계산한다.")
    @MethodSource("providePriceAndExpected")
    void calculateDiscountPrice(Integer price, Integer expected) {
        //when
        final int result = basicPriceDiscountPolicy.calculateDiscountPrice(price);

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
