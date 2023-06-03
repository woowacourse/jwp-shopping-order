package cart.service;

import static cart.fixture.TestFixture.밀리;
import static cart.fixture.TestFixture.밀리_쿠폰_1000원;
import static cart.fixture.TestFixture.밀리_쿠폰_10퍼센트;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import cart.dto.CouponResponse;
import cart.repository.MemberCouponRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class MemberCouponServiceTest {

    @InjectMocks
    private MemberCouponService memberCouponService;

    @Mock
    private MemberCouponRepository memberCouponRepository;

    @Test
    void 멤버의_사용_가능한_모든_쿠폰을_조회한다() {
        given(memberCouponRepository.findNotExpiredAllByMember(밀리))
                .willReturn(List.of(
                        밀리_쿠폰_10퍼센트,
                        밀리_쿠폰_1000원
                ));

        CouponResponse couponResponse = memberCouponService.findAll(밀리);

        assertThat(couponResponse.getFixedCoupon()).hasSize(1);
        assertThat(couponResponse.getRateCoupon()).hasSize(1);
    }

}
