package cart.service;

import static fixture.CouponFixture.정액_할인_쿠폰;
import static fixture.MemberCouponFixture.쿠폰_유저_2_정액_할인_쿠폰;
import static fixture.MemberCouponFixture.쿠폰_유저_2_할인율_쿠폰;
import static fixture.MemberFixture.유저_1;
import static fixture.MemberFixture.유저_2;
import static fixture.MemberFixture.아무것도_가지지_않은_유저;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import anotation.ServiceTest;
import cart.controller.response.CouponResponseDto;
import cart.controller.response.DiscountResponseDto;
import cart.domain.Discount;
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
        couponService.issue(아무것도_가지지_않은_유저, 정액_할인_쿠폰.getId());

        List<CouponResponseDto> couponResponseDtos = couponService.findAllByMember(아무것도_가지지_않은_유저);
        assertThat(couponResponseDtos)
                .extracting(CouponResponseDto::getMemberCouponId, CouponResponseDto::getName)
                .containsExactly(tuple(5L, "정액 할인 쿠폰"));
    }

    @Test
    @DisplayName("사용자가 가지고 있는 Coupon 들을 조회")
    void findAllByMember() {
        List<CouponResponseDto> couponResponseDtos = couponService.findAllByMember(유저_1);

        assertThat(couponResponseDtos)
                .extracting(CouponResponseDto::getMemberCouponId, CouponResponseDto::getName)
                .containsExactly(tuple(1L, "정액 할인 쿠폰"), tuple(2L, "할인율 쿠폰"));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("validateDiscountCalculator")
    @DisplayName("쿠폰을 적용했을 때에 총 가격을 반환")
    void calculateDiscountPrice(String testName, Integer originPrice, Long memberCouponId, DiscountResponseDto expected) {
        DiscountResponseDto discountResponseDto = couponService.calculateDiscountPrice(유저_2, originPrice, memberCouponId);

        assertThat(discountResponseDto)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    private static Stream<Arguments> validateDiscountCalculator() {
        return Stream.of(
                Arguments.of("정액 할인 쿠폰", 10000, 쿠폰_유저_2_정액_할인_쿠폰.getId(), DiscountResponseDto.from(Discount.of(10000, 5000))),
                Arguments.of("할인율 쿠폰", 10000, 쿠폰_유저_2_할인율_쿠폰.getId(), DiscountResponseDto.from(Discount.of(10000, 9000)))
        );
    }

}