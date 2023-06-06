package cart.dao;

import cart.domain.Member;
import cart.domain.coupon.Coupon;
import cart.exception.NotExitingCouponIssueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
class CouponDaoTest {
    @Autowired
    DataSource dataSource;
    JdbcTemplate jdbcTemplate;

    CouponDao couponDao;

    @BeforeEach
    void setting() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        couponDao = new CouponDao(jdbcTemplate);
    }

    /**
     * coupon (id, name, discount)
     * user_coupon (id, member_id, coupon_id )
     */
    @DisplayName("유저의 모든 쿠폰을 가져온다.")
    @Test
    void findCouponById() throws SQLException {
        //given

        //clear
        jdbcTemplate.update("delete from coupon");
        jdbcTemplate.update("delete from user_coupon");

        //targetCoupon
        jdbcTemplate.update("insert into coupon values (1, 1000, '1000')");
        jdbcTemplate.update("insert into coupon values (2, 2000, '2000')");

        //otherCoupon
        jdbcTemplate.update("insert into coupon values (3, 3000, '3000')");

        //targetUser
        jdbcTemplate.update("insert into user_coupon values (1, 1, 1)");
        jdbcTemplate.update("insert into user_coupon values (2, 1, 2)");

        //otherUser
        jdbcTemplate.update("insert into user_coupon values (5, 2, 3)");

        //when
        final List<Coupon> couponById = couponDao.findCouponById(1L);

        //then
        assertThat(couponById).containsExactly(
                new Coupon(1L, 1000, "1000"),
                new Coupon(2L, 2000, "2000")
        );
    }

    @DisplayName("1L의 아이디를 가진 유저에게 1L id coupon 을 준 후에 삭제한다.")
    @Test
    void deleteUserCoupon() throws SQLException {
        //given
        jdbcTemplate.update("insert into user_coupon values (3, 1, 1)");


        final Coupon deletingCoupon = new Coupon(1L, 100, null);
        final List<Coupon> userCoupons = couponDao.findCouponById(1L);
        assertThat(userCoupons).contains(deletingCoupon);

        //when
        couponDao.deleteUserCoupon(new Member(1L, "email", "password"), 1L);

        //then
        assertThat(userCoupons).isNotIn(deletingCoupon);
    }

    @DisplayName("")
    @Test
    void issue_invalid_notExiting() {
        //given
        final Member member = new Member(1L, "a@a.com", "1234");
        final Coupon notExistingCoupon = new Coupon(1_000_000_000, "존재하면 안되는 쿠폰");

        //when, then
        assertThatThrownBy(() -> couponDao.issue(member, notExistingCoupon))
                .isInstanceOf(NotExitingCouponIssueException.class);
    }
}
