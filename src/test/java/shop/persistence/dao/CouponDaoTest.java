package shop.persistence.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import shop.persistence.entity.CouponEntity;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Import(CouponDao.class)
class CouponDaoTest extends DaoTest {
    private static final CouponEntity coupon = new CouponEntity("테스트용 쿠폰", 80,
            365, LocalDateTime.now().plusDays(1));

    @Autowired
    private CouponDao couponDao;

    @DisplayName("쿠폰을 생성할 수 있다.")
    @Test
    void insertCouponTest() {
        //when
        Long couponId = couponDao.insert(coupon);

        //then
        CouponEntity findCoupon = couponDao.findById(couponId);

        assertThat(findCoupon.getName()).isEqualTo(coupon.getName());
        assertThat(findCoupon.getDiscountRate()).isEqualTo(coupon.getDiscountRate());
        assertThat(findCoupon.getPeriod()).isEqualTo(coupon.getPeriod());
    }

    @DisplayName("쿠폰의 이름과 할인율로 쿠폰을 찾을 수 있다.")
    @Test
    void findByNameAndDiscountRateTest() {
        //given
        couponDao.insert(coupon);

        //when
        CouponEntity findCoupon =
                couponDao.findByNameAndDiscountRate(coupon.getName(), coupon.getDiscountRate());

        //then
        assertThat(findCoupon.getName()).isEqualTo(coupon.getName());
        assertThat(findCoupon.getDiscountRate()).isEqualTo(coupon.getDiscountRate());
        assertThat(findCoupon.getPeriod()).isEqualTo(coupon.getPeriod());
    }
}
