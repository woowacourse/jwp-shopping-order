package cart.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import cart.persistence.entity.CouponEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(value = CouponDao.class)
class CouponDaoTest extends DaoTest {

    @Autowired
    private CouponDao couponDao;

    @Test
    @DisplayName("모든 쿠폰 리스트를 반환한다.")
    void getAllCoupons() {
        // given
        final LocalDateTime 저장_시간 = LocalDateTime.now();
        final CouponEntity 신규_가입_축하_쿠폰 = new CouponEntity("신규 가입 축하 쿠폰", 20, 365, 저장_시간.plusDays(365));
        final CouponEntity 첫_주문_감사_쿠폰 = new CouponEntity("첫 주문 감사 쿠폰", 10, 10, 저장_시간.plusDays(10));
        final Long 저장된_신규_가입_축하_쿠폰_아이디 = couponDao.insert(신규_가입_축하_쿠폰);
        final Long 저장된_첫_주문_감사_쿠폰_아이디 = couponDao.insert(첫_주문_감사_쿠폰);

        // when
        final List<CouponEntity> coupons = couponDao.getAllCoupons();

        // then
        assertThat(coupons)
            .extracting(CouponEntity::getId, CouponEntity::getName, CouponEntity::getDiscountRate,
                CouponEntity::getPeriod, CouponEntity::getExpiredDate)
            .containsExactly(
                tuple(저장된_신규_가입_축하_쿠폰_아이디, "신규 가입 축하 쿠폰", 20, 365, 저장_시간.plusDays(365)),
                tuple(저장된_첫_주문_감사_쿠폰_아이디, "첫 주문 감사 쿠폰", 10, 10, 저장_시간.plusDays(10))
            );
    }

    @Test
    @DisplayName("요청받은 아이디에 해당하는 쿠폰을 반환한다.")
    void getCouponById_success() {
        // given
        final LocalDateTime 저장_시간 = LocalDateTime.now();
        final CouponEntity 신규_가입_축하_쿠폰 = new CouponEntity("신규 가입 축하 쿠폰", 20, 365, 저장_시간.plusDays(365));
        final Long 저장된_신규_가입_축하_쿠폰_아이디 = couponDao.insert(신규_가입_축하_쿠폰);

        // when
        final CouponEntity coupon = couponDao.getCouponById(저장된_신규_가입_축하_쿠폰_아이디).get();

        // then
        assertThat(coupon)
            .extracting(CouponEntity::getId, CouponEntity::getName, CouponEntity::getDiscountRate,
                CouponEntity::getPeriod, CouponEntity::getExpiredDate)
            .containsExactly(저장된_신규_가입_축하_쿠폰_아이디, "신규 가입 축하 쿠폰", 20, 365, 저장_시간.plusDays(365));
    }

    @Test
    @DisplayName("요청받은 쿠폰 아이디가 존재하지 않으면 빈 값을 반환한다.")
    void getCouponById_fail() {
        // when
        final Optional<CouponEntity> coupon = couponDao.getCouponById(1L);

        // then
        assertThat(coupon)
            .isEmpty();
    }

    @Test
    @DisplayName("쿠폰을 저장한다.")
    void insert() {
        // given
        final LocalDateTime 저장_시간 = LocalDateTime.now();
        final CouponEntity 신규_가입_축하_쿠폰 = new CouponEntity("신규 가입 축하 쿠폰", 20, 365, 저장_시간.plusDays(365));

        // when
        final Long 저장된_신규_가입_축하_쿠폰_아이디 = couponDao.insert(신규_가입_축하_쿠폰);

        // then
        final CouponEntity coupon = couponDao.getCouponById(저장된_신규_가입_축하_쿠폰_아이디).get();
        assertThat(coupon)
            .extracting(CouponEntity::getId, CouponEntity::getName, CouponEntity::getDiscountRate,
                CouponEntity::getPeriod, CouponEntity::getExpiredDate)
            .containsExactly(저장된_신규_가입_축하_쿠폰_아이디, "신규 가입 축하 쿠폰", 20, 365, 저장_시간.plusDays(365));
    }

    @Test
    @DisplayName("쿠폰을 제거한다.")
    void deleteById() {
        // given
        final LocalDateTime 저장_시간 = LocalDateTime.now();
        final CouponEntity 신규_가입_축하_쿠폰 = new CouponEntity("신규 가입 축하 쿠폰", 20, 365, 저장_시간.plusDays(365));
        final Long 저장된_신규_가입_축하_쿠폰_아이디 = couponDao.insert(신규_가입_축하_쿠폰);

        // when
        couponDao.deleteById(저장된_신규_가입_축하_쿠폰_아이디);

        // then
        final Optional<CouponEntity> coupon = couponDao.getCouponById(저장된_신규_가입_축하_쿠폰_아이디);
        assertThat(coupon)
            .isEmpty();
    }

    @ParameterizedTest(name = "요청받은 쿠폰 이름과 할인율을 가진 쿠폰이 존재하면 true, 아니면 false를 반환한다.")
    @CsvSource(value = {"신규 가입 축하 쿠폰:20:true", "신규 가입 축하 쿠폰:10:false", "첫 주문 감사 쿠폰:20:false"}, delimiter = ':')
    void existByNameAndDiscountRate(final String name, final int discountRate, final boolean expected) {
        // given
        final LocalDateTime 저장_시간 = LocalDateTime.now();
        final CouponEntity 신규_가입_축하_쿠폰 = new CouponEntity("신규 가입 축하 쿠폰", 20, 365, 저장_시간.plusDays(365));
        couponDao.insert(신규_가입_축하_쿠폰);

        // when
        final boolean result = couponDao.existByNameAndDiscountRate(name, discountRate);

        // then
        assertThat(result)
            .isSameAs(expected);
    }
}
