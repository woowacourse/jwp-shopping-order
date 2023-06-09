package cart.application;

import cart.domain.Member;
import cart.dto.coupon.CouponResponse;
import cart.repository.CouponRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static cart.fixtures.CouponFixtures.*;
import static cart.fixtures.MemberFixtures.MEMBER1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @InjectMocks
    CouponService couponService;

    @Mock
    CouponRepository couponRepository;

    @Test
    @DisplayName("회원의 쿠폰 목록을 가져온다.")
    void findByMemberTest() {
        // given
        Member member = MEMBER1;
        when(couponRepository.findByMemberId(member.getId())).thenReturn(List.of(COUPON1, COUPON2, COUPON3));
        List<CouponResponse> expectResponse = List.of(COUPON1_RESPONSE, COUPON2_RESPONSE, COUPON3_RESPONSE);

        // when
        List<CouponResponse> findResponse = couponService.findByMember(member);

        // then
        assertThat(findResponse).isEqualTo(expectResponse);
    }
}