package cart.step2.coupon.service;

import cart.step2.coupon.domain.Coupon;
import cart.step2.coupon.domain.repository.CouponRepository;
import cart.step2.coupon.presentation.dto.CouponResponse;
import cart.step2.coupontype.domain.CouponType;
import cart.step2.coupontype.domain.repository.CouponTypeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @InjectMocks
    private CouponService couponService;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CouponTypeRepository couponTypeRepository;

    @DisplayName("현재 로그인한 멤버가 가지고 있는 모든 쿠폰을 반환한다.")
    @Test
    void getMemberCoupons() {
        // given
        final Long memberId = 1L;
        final List<Coupon> coupons = List.of(
                new Coupon(1L, "N", memberId, 1L),
                new Coupon(2L, "N", memberId, 2L),
                new Coupon(3L, "N", memberId, 3L),
                new Coupon(4L, "N", memberId, 4L)
        );

        doReturn(coupons).when(couponRepository).findAll(1L);
        doReturn(new CouponType(1L, "할인쿠폰1", "1000원 할인 쿠폰", 1000)).when(couponTypeRepository).findById(1L);
        doReturn(new CouponType(2L, "할인쿠폰2", "3000원 할인 쿠폰", 3000)).when(couponTypeRepository).findById(2L);
        doReturn(new CouponType(3L, "할인쿠폰3", "5000원 할인 쿠폰", 5000)).when(couponTypeRepository).findById(3L);
        doReturn(new CouponType(4L, "할인쿠폰4", "10000원 할인 쿠폰", 10000)).when(couponTypeRepository).findById(4L);

        // when
        List<CouponResponse> responses = couponService.getMemberCoupons(memberId);

        // then
        Assertions.assertAll(
                () -> assertThat(responses).extracting(CouponResponse::getId)
                        .contains(1L, 2L, 3L, 4L),
                () -> assertThat(responses).extracting(CouponResponse::getName)
                        .contains("할인쿠폰1", "할인쿠폰2", "할인쿠폰3", "할인쿠폰4"),
                () -> assertThat(responses).extracting(CouponResponse::getDescription)
                        .contains("1000원 할인 쿠폰", "3000원 할인 쿠폰", "5000원 할인 쿠폰", "10000원 할인 쿠폰"),
                () -> assertThat(responses).extracting(CouponResponse::getDiscountAmount)
                        .contains(1000, 3000, 5000, 10000)
        );
    }

    @DisplayName("couponId를 입력받아서 사용한 쿠폰을 삭제한다.")
    @Test
    void deleteByCouponId() {
        // given
        final Long couponId = 1L;
        Coupon coupon = new Coupon(couponId, "Y", 1L, 1L);
        doReturn(coupon).when(couponRepository).findById(couponId);

        // when
        couponService.deleteByCouponId(couponId);

        // then
        verify(couponRepository, times(1)).deleteById(couponId);
    }

    @DisplayName("couponId를 입력받아서 사용하지 않은 쿠폰을 삭제할 수 없다.")
    @Test
    void deleteByCouponIdFail() {
        // given
        final Long couponId = 1L;
        Coupon coupon = new Coupon(couponId, "N", 1L, 1L);
        doReturn(coupon).when(couponRepository).findById(couponId);

        // when, then
        assertThrows(IllegalArgumentException.class, () -> couponService.deleteByCouponId(couponId));
    }

}
