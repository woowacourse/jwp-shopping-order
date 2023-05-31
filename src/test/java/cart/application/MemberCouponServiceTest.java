package cart.application;

import static cart.integration.CouponIntegrationTest.십프로_할인_쿠폰;
import static cart.integration.CouponIntegrationTest.천원_할인_쿠폰;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import cart.domain.MemberCoupon;
import cart.dto.response.MemberCouponResponse;
import cart.repository.MemberCouponRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("MemberCouponService 은(는)")
class MemberCouponServiceTest {

    private MemberCouponRepository memberCouponRepository = mock(MemberCouponRepository.class);
    private MemberCouponService memberCouponService = new MemberCouponService(memberCouponRepository);

    @Test
    void 회원의_모든_쿠폰을_반환한다() {
        // given
        Long memberId = 1L;
        List<MemberCoupon> expected = List.of(
                new MemberCoupon(1L, memberId, 천원_할인_쿠폰),
                new MemberCoupon(2L, memberId, 천원_할인_쿠폰),
                new MemberCoupon(3L, memberId, 십프로_할인_쿠폰)
        );
        given(memberCouponRepository.findAllByMemberId(memberId))
                .willReturn(expected);

        // when
        List<MemberCouponResponse> actual = memberCouponService.findAllByMemberId(memberId);

        // then
        assertThat(actual.size()).isEqualTo(3);
    }
}
