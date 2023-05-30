package cart.application;

import cart.domain.Coupon;
import cart.domain.Coupons;
import cart.domain.Member;
import cart.dto.CouponIssueRequest;
import cart.dto.CouponReissueRequest;
import cart.exception.CannotChangeCouponStatusException;
import cart.exception.CannotDeleteCouponException;
import cart.repository.CouponRepository;
import cart.repository.MemberJdbcRepository;
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
            final Coupon coupon = new Coupon(couponId, "3000원 할인 쿠폰", "상품이 3000원 할인 됩니다.", 3000, true);
            given(memberJdbcRepository.findMemberByMemberIdWithCoupons(request.getId())).willReturn(new Member(1L, "a@a.com", "1234", new Coupons(List.of(coupon))));

            // when
            couponService.reissueCoupon(couponId, request);

            // then
            then(couponRepository).should(only()).changeStatus(any(), any());
        }

        @Test
        void 사용하지_않은_쿠폰이면_실패한다() {
            // given
            final Coupon coupon = new Coupon(couponId, "3000원 할인 쿠폰", "상품이 3000원 할인 됩니다.", 3000, false);
            given(memberJdbcRepository.findMemberByMemberIdWithCoupons(request.getId())).willReturn(new Member(1L, "a@a.com", "1234", new Coupons(List.of(coupon))));

            // when, then
            assertThatThrownBy(() -> couponService.reissueCoupon(couponId, request))
                    .isInstanceOf(CannotChangeCouponStatusException.class);
        }
    }

    @Nested
    class 쿠폰_삭제할_때 {

        private long couponId;
        private long memberId;

        @BeforeEach
        void setUp() {
            couponId = 1L;
            memberId = 1L;
        }

        @Test
        void 성공한다() {
            // given
            final Coupon coupon = new Coupon(couponId, "3000원 할인 쿠폰", "상품이 3000원 할인 됩니다.", 3000, true);
            given(couponRepository.findCouponByCouponIdAndMemberId(anyLong(), anyLong())).willReturn(coupon);

            // when
            couponService.deleteCoupon(couponId, memberId);

            // then
            then(couponRepository).should(times(1)).deleteCoupon(anyLong());
        }

        @Test
        void 사용하지_않은_쿠폰이면_실패한다() {
            // given
            final Coupon coupon = new Coupon(couponId, "3000원 할인 쿠폰", "상품이 3000원 할인 됩니다.", 3000, false);
            given(couponRepository.findCouponByCouponIdAndMemberId(anyLong(), anyLong())).willReturn(coupon);

            // when, then
            assertThatThrownBy(() -> couponService.deleteCoupon(couponId, memberId))
                    .isInstanceOf(CannotDeleteCouponException.class);
        }
    }
}
