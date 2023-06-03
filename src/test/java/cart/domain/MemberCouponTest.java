package cart.domain;

import static fixture.MemberCouponFixture.MEMBER_COUPON_1;
import static fixture.MemberCouponFixture.MEMBER_COUPON_2;
import static fixture.MemberCouponFixture.MEMBER_COUPON_3;

import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MemberCouponTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("validateMember")
    @DisplayName("Member Coupon 의 isSameMember 를 테스트 한다.")
    void isSameMember(String testName, MemberCoupon memberCoupon, MemberCoupon otherMemberCoupon, boolean expected) {
        Assertions.assertThat(memberCoupon.isSameMember(otherMemberCoupon)).isEqualTo(expected);
    }

    private static Stream<Arguments> validateMember() {
        return Stream.of(
                Arguments.of("(False 반환)", MEMBER_COUPON_1, MEMBER_COUPON_3, false),
                Arguments.of("(True 반환)", MEMBER_COUPON_1, MEMBER_COUPON_2, true)
        );
    }

}
