package cart.dao;

import cart.domain.Coupon;
import cart.domain.Member;
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

@JdbcTest
class CouponDaoTest {
    @Autowired
    DataSource dataSource;

    CouponDao couponDao;

    @BeforeEach
    void setting() {
        couponDao = new CouponDao(new JdbcTemplate(dataSource));
    }

    /**
     * coupon (id, name, discount)
     * user_coupon (id, member_id, coupon_id )
     */
    @DisplayName("유저의 모든 쿠폰을 가져온다.")
    @Test
    void findCouponById() throws SQLException {
        //given

        //targetCoupon
        dataSource.getConnection()
                .prepareStatement("insert into coupon values (1, 1000, '1000')").executeUpdate();
        dataSource.getConnection()
                .prepareStatement("insert into coupon values (2, 2000, '2000')").executeUpdate();

        //otherCoupon
        dataSource.getConnection()
                .prepareStatement("insert into coupon values (3, 3000, '3000')").executeUpdate();

        //targetUser
        dataSource.getConnection()
                .prepareStatement("insert into user_coupon values (1, 1, 1)").executeUpdate();
        dataSource.getConnection()
                .prepareStatement("insert into user_coupon values (2, 1, 2)").executeUpdate();
        //otherUser
        dataSource.getConnection()
                .prepareStatement("insert into user_coupon values (3, 2, 3)").executeUpdate();

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
        dataSource.getConnection()
                .prepareStatement("insert into coupon values (1, 1000, '1000')").executeUpdate();

        dataSource.getConnection()
                .prepareStatement("insert into user_coupon values (1, 1, 1)").executeUpdate();

        final Coupon deletingCoupon = new Coupon(1L, 100, null);
        final List<Coupon> userCoupons = couponDao.findCouponById(1L);
        assertThat(userCoupons).contains(deletingCoupon);

        //when
        couponDao.deleteUserCoupon(new Member(1L, "email", "password"), 1L);

        //then
        assertThat(userCoupons).isNotIn(deletingCoupon);
    }
}
