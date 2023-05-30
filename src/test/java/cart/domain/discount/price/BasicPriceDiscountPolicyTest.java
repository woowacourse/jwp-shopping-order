package cart.domain.discount.price;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import cart.domain.price.discount.DiscountInformation;
import cart.domain.price.discount.grade.Grade;
import cart.domain.price.discount.price.BasicPriceDiscountPolicy;
import cart.fixture.Fixture;

class BasicPriceDiscountPolicyTest {

    BasicPriceDiscountPolicy basicPriceDiscountPolicy = new BasicPriceDiscountPolicy();

    @ParameterizedTest(name = "금액에 따른 할인 금액을 계산한다.")
    @MethodSource("providePriceAndExpected")
    void calculateDiscountPrice(Integer price, Integer expected) {
        //when
        final int result = basicPriceDiscountPolicy.calculateDiscountPrice(price, Fixture.GOLD_MEMBER);

        //then
        assertThat(result).isEqualTo(expected);
    }

    public static Stream<Arguments> providePriceAndExpected() {
        return Stream.of(
                Arguments.of(9000, 90),
                Arguments.of(19000, 380),
                Arguments.of(29000, 870),
                Arguments.of(110000, 11000)
        );
    }

    @ParameterizedTest(name = "금액에 따른 상세 할인 정보를 반환한다.")
    @MethodSource("provideParamsAndExpected")
    void computeDiscountInformation(Integer price, Grade grade, DiscountInformation expected) {
        //when
        final var result = basicPriceDiscountPolicy.computeDiscountInformation(price, grade);

        //then
        Assertions.assertAll(
                () -> assertThat(result.getPolicyName()).isEqualTo(expected.getPolicyName()),
                () -> assertThat(result.getDiscountRate()).isEqualTo(expected.getDiscountRate()),
                () -> assertThat(result.getDiscountPrice()).isEqualTo(expected.getDiscountPrice())
        );
    }

    public static Stream<Arguments> provideParamsAndExpected() {
        return Stream.of(
                Arguments.of(9000, Grade.GOLD, new DiscountInformation("priceDiscount", 0.01, 90)),
                Arguments.of(19000, Grade.GOLD, new DiscountInformation("priceDiscount", 0.02, 380)),
                Arguments.of(29000, Grade.GOLD, new DiscountInformation("priceDiscount", 0.03, 870)),
                Arguments.of(110000, Grade.GOLD, new DiscountInformation("priceDiscount", 0.1, 11000))
        );
    }
}
