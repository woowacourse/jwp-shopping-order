package cart.step2.coupon.service;

import cart.step2.coupon.domain.Coupon;
import cart.step2.coupon.domain.repository.CouponRepository;
import cart.step2.coupontype.domain.repository.CouponTypeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @DisplayName("couponId를 입력받아서 사용한 쿠폰을 삭제한다.")
    @Test
    void deleteByCouponId() {
        // given
        final Long couponId = 1L;
        Coupon coupon = new Coupon(couponId, 1, 1L, 1L);
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
        Coupon coupon = new Coupon(couponId, 0, 1L, 1L);
        doReturn(coupon).when(couponRepository).findById(couponId);

        // when, then
        assertThrows(IllegalArgumentException.class, () -> couponService.deleteByCouponId(couponId));
    }

}
