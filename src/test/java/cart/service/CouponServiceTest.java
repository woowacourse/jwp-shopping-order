package cart.service;

import static fixture.MemberCouponFixture.MEMBER_COUPON_3;
import static fixture.MemberCouponFixture.MEMBER_COUPON_4;
import static fixture.MemberFixture.MEMBER_1;
import static fixture.MemberFixture.MEMBER_2;
import static fixture.MemberFixture.MEMBER_3;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import anotation.ServiceTest;
import cart.controller.response.CouponResponseDto;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class CouponServiceTest {

    @Autowired
    private CouponService couponService;

    @Test
    @DisplayName("issue 테스트")
    void issue() {
        couponService.issue(MEMBER_3);

        List<CouponResponseDto> couponResponseDtos = couponService.findAllByMember(MEMBER_3);
        assertThat(couponResponseDtos)
                .extracting(CouponResponseDto::getMemberCouponId, CouponResponseDto::getName)
                .containsExactly(tuple(5L, "정액 할인 쿠폰"), tuple(6L, "할인율 쿠폰"));
    }

    @Test
    @DisplayName("사용자가 가지고 있는 Coupon 들을 조회")
    void findAllByMember() {
        List<CouponResponseDto> couponResponseDtos = couponService.findAllByMember(MEMBER_1);

        assertThat(couponResponseDtos)
                .extracting(CouponResponseDto::getMemberCouponId, CouponResponseDto::getName)
                .containsExactly(tuple(1L, "정액 할인 쿠폰"), tuple(2L, "할인율 쿠폰"));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("validateDiscountCalculator")
    @DisplayName("쿠폰을 적용했을 때에 총 가격을 반환")
    void calculateDiscountPrice(String testName, Integer originPrice, Long memberCouponId,Integer expectedValue) {
        Integer money = couponService.calculateDiscountPrice(MEMBER_2, originPrice, memberCouponId);

        assertThat(money).isEqualTo(expectedValue);
    }

    private static Stream<Arguments> validateDiscountCalculator() {
        return Stream.of(
                Arguments.of("정액 할인 쿠폰", 10000, MEMBER_COUPON_3.getId(), 5000),
                Arguments.of("할인율 쿠폰", 10000, MEMBER_COUPON_4.getId(), 9000)
        );
    }

}