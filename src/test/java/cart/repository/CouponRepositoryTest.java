package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import cart.dao.CouponDao;
import cart.domain.Money;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponType;
import java.math.BigDecimal;
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
class CouponRepositoryTest {

    @InjectMocks
    private CouponRepository couponRepository;

    @Mock
    private CouponDao couponDao;

    @Test
    void 쿠폰을_저장한다() {
        // given
        given(couponDao.save(any()))
                .willReturn(1L);

        // when
        Coupon coupon = new Coupon("쿠폰", CouponType.RATE, BigDecimal.valueOf(10), new Money(1000));
        Coupon savedCoupon = couponRepository.save(coupon);

        // then
        assertThat(savedCoupon.getId()).isEqualTo(1L);
        assertThat(savedCoupon).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(coupon);
    }
}
