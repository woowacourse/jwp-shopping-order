package cart.step2.coupon.persist;

import cart.step2.coupon.domain.Coupon;
import cart.step2.coupon.domain.CouponEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class CouponRepositoryMapperTest {

    @InjectMocks
    private CouponRepositoryMapper couponRepositoryMapper;

    @Mock
    private CouponDao couponDao;

    @DisplayName("memberId를 입력받고 List<CouponEntity>을 반환받아 List<Coupon>으로 반환한다.")
    @Test
    void findAll() {
        // given
        final Long memberId = 1L;
        List<CouponEntity> coupons = List.of(
                new CouponEntity(1L, "N", memberId, 1L),
                new CouponEntity(2L, "N", memberId, 2L),
                new CouponEntity(3L, "N", memberId, 3L),
                new CouponEntity(4L, "N", memberId, 4L)
        );
        doReturn(coupons).when(couponDao).findAll(memberId);
        
        // when
        List<Coupon> responses = couponRepositoryMapper.findAll(memberId);

        // then
        assertAll(
                () -> assertThat(responses.get(0).getClass()).isEqualTo(Coupon.class),
                () -> assertThat(responses).extracting(Coupon::getMemberId)
                        .contains(1L, 1L, 1L, 1L),
                () -> assertThat(responses).extracting(Coupon::getId)
                        .contains(1L, 2L, 3L, 4L),
                () -> assertThat(responses).extracting(Coupon::getCouponTypeId)
                        .contains(1L, 2L, 3L, 4L),
                () -> assertThat(responses).extracting(Coupon::getUsageStatus)
                        .contains("N", "N", "N", "N")
        );
    }

    @DisplayName("couponId를 입력받아 Optional<CouponEntity>를 반환받고 Coupon으로 반환한다.")
    @Test
    void findById() {
        // given
        final Long memberId = 1L;
        final Long couponTypeId = 1L;
        Optional<CouponEntity> couponEntityOptional = Optional.of(new CouponEntity(1L, "N", memberId, couponTypeId));
        doReturn(couponEntityOptional).when(couponDao).findById(memberId);

        // when
        Coupon coupon = couponRepositoryMapper.findById(couponTypeId);

        // then
        Assertions.assertAll(
                () -> assertThat(coupon.getId()).isEqualTo(1L),
                () -> assertThat(coupon.getMemberId()).isEqualTo(memberId),
                () -> assertThat(coupon.getCouponTypeId()).isEqualTo(couponTypeId),
                () -> assertThat(coupon.getUsageStatus()).isEqualTo("N")
        );
    }

}
