package cart.dao;

import cart.entity.CouponEntity;
import cart.entity.MemberCouponEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
class MemberCouponDaoTest {

    private MemberCouponDao memberCouponDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MemberCouponEntity memberCoupon;

    @BeforeEach
    void setUp() {
        memberCouponDao = new MemberCouponDao(jdbcTemplate);
        memberCoupon = new MemberCouponEntity(1L, 1L, true);
    }

    @Test
    @DisplayName("사용자의 유효한 쿠폰을 찾는다.")
    void findAvailableCouponByMember() {
        Long userCouponId = memberCouponDao.save(memberCoupon);
        CouponEntity coupon = memberCouponDao.findAvailableCouponByIdAndMemberId(1L, userCouponId).get();
        assertAll(
                () -> assertThat(coupon.getName()).isEqualTo("5000원 할인 쿠폰"),
                () -> assertThat(coupon.getDiscountType()).isEqualTo("deduction"),
                () -> assertThat(coupon.getDiscountPrice()).isEqualTo(5000)
        );
    }

    @Test
    @DisplayName("사용자의 유효한 쿠폰을 유효하지 않은 쿠폰으로 변경한다.")
    void changeUserUsedCouponAvailability() {
        Long userCouponId = memberCouponDao.save(memberCoupon);
        memberCouponDao.updateUsedCouponAvailabilityById(userCouponId);

        assertThat(memberCouponDao.checkByCouponIdAndMemberId(1L, userCouponId)).isFalse();
    }

    @Test
    @DisplayName("사용자의 쿠폰을 id로 조회한다.")
    void checkMemberCouponById() {
        Long userCouponId = memberCouponDao.save(memberCoupon);

        assertThat(memberCouponDao.checkByCouponIdAndMemberId(1L, userCouponId)).isTrue();
    }

    @Test
    @DisplayName("사용자 쿠폰을 발급한다.")
    void createUserCoupon() {
        memberCouponDao.save(memberCoupon);
        CouponEntity coupon = memberCouponDao.findAllByMemberId(1L).get(0);
        assertAll(
                () -> assertThat(coupon.getName()).isEqualTo("5000원 할인 쿠폰"),
                () -> assertThat(coupon.getDiscountType()).isEqualTo("deduction"),
                () -> assertThat(coupon.getDiscountPrice()).isEqualTo(5000)
        );
    }

    @Test
    @DisplayName("사용자 id로 쿠폰을 찾는다.")
    void findCouponByMemberId() {
        memberCouponDao.save(memberCoupon);
        List<CouponEntity> coupons = memberCouponDao.findAllByMemberId(1L);
        CouponEntity coupon = coupons.get(0);
        assertAll(
                () -> assertThat(coupon.getName()).isEqualTo("5000원 할인 쿠폰"),
                () -> assertThat(coupon.getDiscountType()).isEqualTo("deduction"),
                () -> assertThat(coupon.getDiscountPrice()).isEqualTo(5000)
        );
    }

    @Test
    @DisplayName("사용자의 유효하지 않은 쿠폰을 유효한 쿠폰으로 바꾼다.")
    void changeUserUnUsedCouponAvailability() {
        Long userCouponId = memberCouponDao.save(memberCoupon);
        memberCouponDao.updateUsedCouponAvailabilityById(userCouponId);
        assertThat(memberCouponDao.checkByCouponIdAndMemberId(1L, 1L)).isFalse();
        memberCouponDao.updateUnUsedCouponAvailabilityById(userCouponId);
        assertThat(memberCouponDao.checkByCouponIdAndMemberId(1L, 1L)).isTrue();
    }
}
