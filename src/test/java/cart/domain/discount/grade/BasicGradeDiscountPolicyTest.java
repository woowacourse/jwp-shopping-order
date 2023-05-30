package cart.domain.discount.grade;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import cart.domain.Member;
import cart.domain.price.discount.DiscountInformation;
import cart.domain.price.discount.grade.BasicGradeDiscountPolicy;
import cart.domain.price.discount.grade.Grade;
import cart.fixture.Fixture;

class BasicGradeDiscountPolicyTest {
    BasicGradeDiscountPolicy basicGradeDiscountPolicy = new BasicGradeDiscountPolicy();

    @ParameterizedTest(name = "멤버의 등급별 할인 금액을 계산한다.")
    @MethodSource("provideMemberAndPrice")
    void calculateDiscountPrice(Integer price, Member member, Integer expected) {
        //when
        final int result = basicGradeDiscountPolicy.calculateDiscountPrice(price, member);

        //then
        assertThat(result).isEqualTo(expected);
    }

    public static Stream<Arguments> provideMemberAndPrice() {
        return Stream.of(
                Arguments.of(20000, Fixture.GOLD_MEMBER, 1000),
                Arguments.of(20000, Fixture.SILVER_MEMBER, 600),
                Arguments.of(20000, Fixture.BRONZE_MEMBER, 200),
                Arguments.of(100000, Fixture.GOLD_MEMBER, 5000)
        );
    }

    @ParameterizedTest(name = "가격과 등급을 통해 상세 할인정보를 반환한다.")
    @MethodSource("provideParamsAndExpected")
    void computeDiscountInformation(Integer price, Grade grade, DiscountInformation expected) {
        final var result = basicGradeDiscountPolicy.computeDiscountInformation(price, grade);

        //then
        Assertions.assertAll(
                () -> assertThat(result.getPolicyName()).isEqualTo(expected.getPolicyName()),
                () -> assertThat(result.getDiscountRate()).isEqualTo(expected.getDiscountRate()),
                () -> assertThat(result.getDiscountPrice()).isEqualTo(expected.getDiscountPrice())
        );
    }

    public static Stream<Arguments> provideParamsAndExpected() {
        return Stream.of(
                Arguments.of(20000, Grade.GOLD, new DiscountInformation("gradeDiscount", 0.05, 1000 )),
                Arguments.of(20000, Grade.SILVER, new DiscountInformation("gradeDiscount", 0.03, 600 )),
                Arguments.of(20000, Grade.BRONZE, new DiscountInformation("gradeDiscount", 0.01, 200 )),
                Arguments.of(100000, Grade.GOLD, new DiscountInformation("gradeDiscount", 0.05, 5000 ))
        );
    }
}
