package cart.service.coupon;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.Coupons;
import cart.dto.coupon.CouponCreateRequest;
import cart.dto.coupon.CouponResponse;
import cart.exception.CouponCreateBadRequestException;
import cart.exception.CouponNotFoundException;
import cart.exception.MemberNotFoundException;
import cart.repository.coupon.CouponRepository;
import cart.repository.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;

import static cart.fixture.CouponFixture.createCoupons;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CouponServiceUnitTest {

    @InjectMocks
    private CouponService couponService;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private MemberRepository memberRepository;

    @DisplayName("쿠폰을 모두 찾는다.")
    @Test
    void find_all_coupons() {
        // given
        Coupons coupons = createCoupons();
        Coupon coupon = coupons.getCoupons().get(0);

        List<CouponResponse> expected = coupons.getCoupons().stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList());

        given(couponRepository.findAll()).willReturn(coupons);

        // when
        List<CouponResponse> result = couponService.findAllCoupons();

        // then
        assertAll(
                () -> assertThat(result.size()).isEqualTo(expected.size()),
                () -> assertThat(result.get(0).getCouponName()).isEqualTo(expected.get(0).getCouponName())
        );
    }

    @DisplayName("쿠폰을 id로 찾는다.")
    @Test
    void find_coupon_by_id() {
        // given
        Long couponId = 1L;
        Coupon coupon = createCoupons().getCoupons().get(0);
        given(couponRepository.findById(couponId)).willReturn(coupon);

        // when
        CouponResponse result = couponService.findById(couponId);

        // then
        assertThat(result.getCouponName()).isEqualTo(coupon.getName());
    }

    @DisplayName("쿠폰을 생성한다.")
    @Test
    void create_coupon() {
        // given
        CouponCreateRequest req = new CouponCreateRequest("쿠폰", true, 10);

        // when
        long id = couponService.createCoupon(req);

        // then
        verify(couponRepository).save(req.getName(), req.getIsPercentage(), req.getAmount());
    }

    @DisplayName("쿠폰 생성시 정률이 올바르지 않으면 예외를 던진다.")
    @Test
    void throws_exception_when_invalid_amount() {
        // given
        CouponCreateRequest req = new CouponCreateRequest("쿠폰", true, 101);

        // when & then
        assertThatThrownBy(() -> couponService.createCoupon(req))
                .isInstanceOf(CouponCreateBadRequestException.class);
    }

    @DisplayName("쿠폰을 id 값을 기준으로 제거한다.")
    @Test
    void delete_coupon_by_id() {
        // given
        long id = 1L;

        // when
        couponService.deleteCoupon(id);

        // then
        verify(couponRepository).deleteById(id);
    }

    @DisplayName("멤버를 찾을 수 없으면 예외를 발생시킨다.")
    @Test
    void throws_exception_when_member_not_found() {
        // given
        given(memberRepository.isExistMemberById(any())).willReturn(false);

        // when & then
        assertThatThrownBy(() -> couponService.giveCouponToMember(1L, 3L))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @DisplayName("쿠폰을 찾을 수 없으면 예외를 발생시킨다.")
    @Test
    void throws_exception_when_coupon_not_found() {
        // given
        given(memberRepository.isExistMemberById(any())).willReturn(true);
        given(couponRepository.isExistCouponById(any())).willReturn(false);

        // when & then
        assertThatThrownBy(() -> couponService.giveCouponToMember(1L, 3L))
                .isInstanceOf(CouponNotFoundException.class);
    }
}
