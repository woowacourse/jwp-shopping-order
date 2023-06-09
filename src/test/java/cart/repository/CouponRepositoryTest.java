package cart.repository;

import static cart.fixture.CouponFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import cart.dao.CouponDao;
import cart.dao.entity.CouponEntity;
import cart.domain.coupon.Coupon;
import cart.exception.CouponException;
import cart.fixture.CouponFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CouponRepositoryTest {
    @Mock
    private CouponDao couponDao;

    @InjectMocks
    private CouponRepository couponRepository;

    @Test
    public void 아이디로_조회한다() {
        when(couponDao.findById(테스트쿠폰1.getCouponInfo().getId())).thenReturn(Optional.of(테스트쿠폰1_엔티티));

        Coupon coupon = couponRepository.findById(테스트쿠폰1.getCouponInfo().getId());

        assertThat(coupon).isEqualTo(테스트쿠폰1);
    }

    @Test
    public void 아이디로_조회시_존재하지_않으면_예외를_발생한다() {
        Long couponId = 1L;
        when(couponDao.findById(couponId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> couponRepository.findById(couponId))
                .isInstanceOf(CouponException.NoExist.class);
    }

    @Test
    public void 모든_쿠폰을_조회한다() {
        List<CouponEntity> allCouponEntities = Arrays.asList(테스트쿠폰1_엔티티, 테스트쿠폰2_엔티티);
        when(couponDao.findAll()).thenReturn(allCouponEntities);

        List<Coupon> coupons = couponRepository.findAllCoupons();

        assertThat(coupons).contains(테스트쿠폰1, 테스트쿠폰2);
    }
}
