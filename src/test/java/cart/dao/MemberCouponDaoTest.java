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
        Long userCouponId = memberCouponDao.createUserCoupon(memberCoupon);
        CouponEntity coupon = memberCouponDao.findAvailableCouponByMember(1L, userCouponId).get();
        assertAll(
                () -> assertThat(coupon.getName()).isEqualTo("5000원 할인 쿠폰"),
                () -> assertThat(coupon.getDiscountType()).isEqualTo("deduction"),
                () -> assertThat(coupon.getDiscountPrice()).isEqualTo(5000)
        );
    }

    @Test
    @DisplayName("사용자의 유효한 쿠폰을 유효하지 않은 쿠폰으로 변경한다.")
    void changeUserUsedCouponAvailability() {
        Long userCouponId = memberCouponDao.createUserCoupon(memberCoupon);
        memberCouponDao.changeUserUsedCouponAvailability(userCouponId);

        assertThat(memberCouponDao.checkMemberCouponById(1L, userCouponId)).isFalse();
    }

    @Test
    @DisplayName("사용자의 쿠폰을 id로 조회한다.")
    void checkMemberCouponById() {
        Long userCouponId = memberCouponDao.createUserCoupon(memberCoupon);

        assertThat(memberCouponDao.checkMemberCouponById(1L, userCouponId)).isTrue();
    }

    @Test
    @DisplayName("사용자 쿠폰을 발급한다.")
    void createUserCoupon() {
        memberCouponDao.createUserCoupon(memberCoupon);
        CouponEntity coupon = memberCouponDao.findCouponByMemberId(1L).get(0);
        assertAll(
                () -> assertThat(coupon.getName()).isEqualTo("5000원 할인 쿠폰"),
                () -> assertThat(coupon.getDiscountType()).isEqualTo("deduction"),
                () -> assertThat(coupon.getDiscountPrice()).isEqualTo(5000)
        );
    }

    @Test
    @DisplayName("사용자 id로 쿠폰을 찾는다.")
    void findCouponByMemberId() {
        memberCouponDao.createUserCoupon(memberCoupon);
        List<CouponEntity> coupons = memberCouponDao.findCouponByMemberId(1L);
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
        Long userCouponId = memberCouponDao.createUserCoupon(memberCoupon);
        memberCouponDao.changeUserUsedCouponAvailability(userCouponId);
        assertThat(memberCouponDao.checkMemberCouponById(1L, 1L)).isFalse();
        memberCouponDao.changeUserUnUsedCouponAvailability(userCouponId);
        assertThat(memberCouponDao.checkMemberCouponById(1L, 1L)).isTrue();
    }
}
