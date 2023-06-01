package cart.application;

import static cart.fixture.TestFixture.COUPON_FIXED_2000;
import static cart.fixture.TestFixture.MEMBER_A;
import static cart.fixture.TestFixture.MEMBER_A_COUPON_FIXED_2000;
import static cart.fixture.TestFixture.MEMBER_A_COUPON_PERCENTAGE_50;
import static cart.fixture.TestFixture.MEMBER_B;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.dao.dto.MemberCouponDto;
import cart.exception.InaccessibleCouponException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    CouponDao couponDao;
    @Mock
    MemberCouponDao memberCouponDao;
    @Mock
    MemberService memberService;
    @InjectMocks
    private CouponService couponService;

    @BeforeEach
    void setUp() {
        when(couponDao.selectBy(eq(COUPON_FIXED_2000().getId()))).thenReturn(COUPON_FIXED_2000());
    }

    @Test
    void 멤버의_쿠폰을_모두_조회한다() {
        when(memberService.getMemberBy(MEMBER_A.getId())).thenReturn(MEMBER_A);
        when(memberCouponDao.selectAllBy(MEMBER_A.getId())).thenReturn(List.of(
                MemberCouponDto.of(MEMBER_A_COUPON_FIXED_2000()),
                MemberCouponDto.of(MEMBER_A_COUPON_PERCENTAGE_50())
        ));

        assertThat(couponService.getMemberCouponsOf(MEMBER_A))
                .containsExactlyInAnyOrder(MEMBER_A_COUPON_FIXED_2000(), MEMBER_A_COUPON_PERCENTAGE_50());
    }

    @Test
    void id로_사용자_쿠폰을_가져온다() {
        when(memberService.getMemberBy(MEMBER_A.getId())).thenReturn(MEMBER_A);
        when(memberCouponDao.selectBy(eq(MEMBER_A_COUPON_FIXED_2000().getId()))).thenReturn(
                MemberCouponDto.of(MEMBER_A_COUPON_FIXED_2000()));

        assertThat(couponService.getMemberCouponBy(MEMBER_A, MEMBER_A_COUPON_FIXED_2000().getId()))
                .isEqualTo(MEMBER_A_COUPON_FIXED_2000());
    }

    @Test
    void 다른_사용자의_쿠폰을_가져올_수_없다() {
        when(memberService.getMemberBy(MEMBER_A.getId())).thenReturn(MEMBER_A);
        when(memberCouponDao.selectBy(eq(MEMBER_A_COUPON_FIXED_2000().getId()))).thenReturn(
                MemberCouponDto.of(MEMBER_A_COUPON_FIXED_2000()));

        assertThatThrownBy(() -> couponService.getMemberCouponBy(MEMBER_B, MEMBER_A_COUPON_FIXED_2000().getId()))
                .isInstanceOf(InaccessibleCouponException.class);
    }

    @Test
    void id로_쿠폰을_가져온다() {
        assertThat(couponService.getCouponBy(COUPON_FIXED_2000().getId()))
                .isEqualTo(COUPON_FIXED_2000());
    }
}
