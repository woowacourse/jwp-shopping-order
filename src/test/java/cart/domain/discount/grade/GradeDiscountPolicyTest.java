package cart.domain.discount.grade;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import cart.domain.Member;

class GradeDiscountPolicyTest {
    GradeDiscountPolicy gradeDiscountPolicy = new GradeDiscountPolicy();

    @ParameterizedTest(name = "멤버의 등급별 할인 금액을 계산한다.")
    @MethodSource("provideMemberAndPrice")
    void calculateDiscountPrice(Integer price, Member member, Integer expected) {
        //when
        final int result = gradeDiscountPolicy.calculateDiscountPrice(price, member);

        //then
        assertThat(result).isEqualTo(expected);
    }

    public static Stream<Arguments> provideMemberAndPrice() {
        return Stream.of(
                Arguments.of(20000, new Member(1L, "a@a.com", "1234", 1), 1000),
                Arguments.of(20000, new Member(1L, "a@a.com", "1234", 2), 600),
                Arguments.of(20000, new Member(1L, "a@a.com", "1234", 3), 200)
        );
    }
}
