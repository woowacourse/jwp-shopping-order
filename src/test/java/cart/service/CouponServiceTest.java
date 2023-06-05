package cart.service;

import cart.controller.dto.CouponReissueRequest;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponRepository;
import cart.domain.coupon.Coupons;
import cart.domain.member.Member;
import cart.exception.CannotChangeCouponStatusException;
import cart.exception.CannotDeleteCouponException;
import cart.repository.MemberJdbcRepository;
import cart.service.coupon.CouponService;
import cart.service.dto.CouponReissueDto;
import cart.service.dto.CouponSaveDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @InjectMocks
    private CouponService couponService;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private MemberJdbcRepository memberJdbcRepository;

    private Coupon unUsedCoupon;
    private Coupon usedCoupon;
    private long couponId;

    @BeforeEach
    void setUp() {
        couponId = 1L;
        usedCoupon = new Coupon(couponId, 1L, "3000원 할인 쿠폰", "상품이 3000원 할인 됩니다.", 3000, true);
        unUsedCoupon = new Coupon(couponId, 1L, "3000원 할인 쿠폰", "상품이 3000원 할인 됩니다.", 3000, false);
    }

    @Test
    void 쿠폰을_발급한다() {
        // given
        given(couponRepository.issue(any(Member.class), anyLong())).willReturn(1L);
        final CouponSaveDto saveDto = new CouponSaveDto(1L, "a@a.com", "1234", 1L);

        // when
        final Long saveId = couponService.issueCoupon(saveDto);

        // then
        assertThat(saveId).isEqualTo(1L);
    }

    @Nested
    class 쿠폰_재발급할_때 {

        private CouponReissueRequest request;
        private long couponId;

        @BeforeEach
        void setUp() {
            request = new CouponReissueRequest(1L, "a@a.com", "1234");
            couponId = 1L;
        }

        @Test
        void 성공한다() {
            // given
            final long memberId = 1L;
            final Member member = new Member(memberId, "a@a.com", "1234", new Coupons(List.of(usedCoupon)));
            given(memberJdbcRepository.findMemberByMemberIdWithCoupons(request.getId())).willReturn(member);

            // when
            couponService.reissueCoupon(new CouponReissueDto(couponId, memberId));

            // then
            then(couponRepository).should(only()).changeStatusTo(any(), any());
        }

        @Test
        void 사용하지_않은_쿠폰이면_실패한다() {
            // given
            given(memberJdbcRepository.findMemberByMemberIdWithCoupons(request.getId())).willReturn(new Member(1L, "a@a.com", "1234", new Coupons(List.of(unUsedCoupon))));

            // when, then
            assertThatThrownBy(() -> couponService.reissueCoupon(new CouponReissueDto(couponId, request.getId())))
                    .isInstanceOf(CannotChangeCouponStatusException.class);
        }
    }

    @Nested
    class 쿠폰_삭제할_때 {

        private long couponId;

        @BeforeEach
        void setUp() {
            couponId = 1L;
        }

        @Test
        void 성공한다() {
            // given
            given(couponRepository.findCouponById(anyLong())).willReturn(usedCoupon);

            // when
            couponService.deleteCoupon(couponId);

            // then
            then(couponRepository).should(times(1)).deleteCoupon(anyLong());
        }

        @Test
        void 사용하지_않은_쿠폰이면_실패한다() {
            // given
            given(couponRepository.findCouponById(anyLong())).willReturn(unUsedCoupon);

            // when, then
            assertThatThrownBy(() -> couponService.deleteCoupon(couponId))
                    .isInstanceOf(CannotDeleteCouponException.class);
        }
    }
}
