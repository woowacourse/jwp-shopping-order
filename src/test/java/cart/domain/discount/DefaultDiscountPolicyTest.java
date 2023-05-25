package cart.domain.discount;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import cart.domain.Member;
import cart.domain.discount.grade.GradeDiscountPolicy;
import cart.domain.discount.price.PriceDiscountPolicy;
import cart.fixture.Fixture;

class DefaultDiscountPolicyTest {

    DefaultDiscountPolicy discountPolicy = new DefaultDiscountPolicy(
            List.of(new GradeDiscountPolicy(), new PriceDiscountPolicy())
    );

    @ParameterizedTest(name = "price와 member를 통해 총 할인 금액을 계산한다.")
    @MethodSource("provideParamsAndExpected")
    void calculateDiscountPrice(int price, Member member, int expected) {
        //when
        final int result = discountPolicy.calculateDiscountPrice(price, member);

        //then
        assertThat(result).isEqualTo(expected);
    }
    
    public static Stream<Arguments> provideParamsAndExpected() {
        return Stream.of(
                Arguments.of(10000, Fixture.GOLD_MEMBER, 700),
                Arguments.of(20000, Fixture.SILVER_MEMBER, 1200),
                Arguments.of(30000, Fixture.BRONZE_MEMBER, 1500)
        );
    }
}
