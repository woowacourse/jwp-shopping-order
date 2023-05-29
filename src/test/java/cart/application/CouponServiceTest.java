package cart.application;

import cart.domain.Member;
import cart.dto.CouponIssueRequest;
import cart.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @InjectMocks
    private CouponService couponService;

    @Mock
    private CouponRepository couponRepository;

    @Test
    void 쿠폰을_발급한다() {
        // given
        final Member member = new Member(1L, "a@a.com", "1234");
        final CouponIssueRequest request = new CouponIssueRequest(1L);
        given(couponRepository.issue(member, 1L)).willReturn(1L);

        // when
        final Long saveId = couponService.issueCoupon(member, request);

        // then
        assertThat(saveId).isEqualTo(1L);
    }
}
