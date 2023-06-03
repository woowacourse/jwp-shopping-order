package cart.service;

import static fixture.MemberFixture.MEMBER_1;
import static fixture.MemberFixture.MEMBER_3;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import anotation.ServiceTest;
import cart.controller.response.CouponResponseDto;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

}