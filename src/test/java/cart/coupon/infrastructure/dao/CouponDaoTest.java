package cart.coupon.infrastructure.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.common.DaoTest;
import cart.coupon.domain.DiscountType;
import cart.coupon.domain.TargetType;
import cart.coupon.infrastructure.entity.CouponEntity;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("CouponDao 은(는)")
@DaoTest
class CouponDaoTest {

    @Autowired
    private CouponDao couponDao;

    @Test
    void 쿠폰을_저장한다() {
        // given
        CouponEntity coupon = new CouponEntity(
                null,
                "말랑이 멋진쿠폰",
                1L,
                DiscountType.RATE,
                TargetType.ALL,
                1L,
                50);

        // when
        Long id = couponDao.save(coupon);

        // then
        assertThat(id).isNotNull();
    }

    @Test
    void 쿠폰을_조회한다() {
        // given
        CouponEntity coupon = new CouponEntity(
                null,
                "말랑이 멋진쿠폰",
                1L,
                DiscountType.RATE,
                TargetType.ALL,
                1L,
                50);
        Long id = couponDao.save(coupon);

        // when
        CouponEntity find = couponDao.findById(id).get();

        // then
        assertThat(find).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(coupon);
    }

    @Test
    void 특정_회원의_모든_쿠폰을_조회한다() {
        // given
        CouponEntity coupon1 = new CouponEntity(
                null,
                "말랑이 멋진쿠폰",
                1L,
                DiscountType.RATE,
                TargetType.ALL,
                1L,
                50);
        Long id1 = couponDao.save(coupon1);
        CouponEntity coupon2 = new CouponEntity(
                null,
                "말랑이 더 멋진쿠폰",
                1L,
                DiscountType.RATE,
                TargetType.ALL,
                1L,
                70);
        Long id2 = couponDao.save(coupon2);

        // when
        List<CouponEntity> allByMemberId = couponDao.findAllByMemberId(1L);

        // then
        assertThat(allByMemberId).hasSize(2);
    }
}
