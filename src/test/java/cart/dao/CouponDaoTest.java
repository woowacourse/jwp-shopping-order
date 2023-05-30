package cart.dao;

import cart.domain.Coupon;
import cart.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
class CouponDaoTest {
    public static final Coupon COUPON1_10 = new Coupon("쿠폰1", 10);
    public static final Coupon COUPON2_20 = new Coupon("쿠폰2", 20);
    private CouponDao couponDao;
    private MemberDao memberDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        couponDao = new CouponDao(jdbcTemplate);
        memberDao = new MemberDao(jdbcTemplate);
    }

    @Test
    @DisplayName("쿠폰을 생성한다.")
    void createCoupon() {
        Long id = couponDao.createCoupon(COUPON1_10);

        assertThat(id).isNotNull();
    }

    @Test
    @DisplayName("쿠폰을 삭제한다.")
    void deleteCoupon() {
        Long id = couponDao.createCoupon(COUPON1_10);

        couponDao.deleteCoupon(id);

        assertThatThrownBy(() -> couponDao.getCouponById(id))
                .isInstanceOf(EmptyResultDataAccessException.class);

    }

    @Test
    @DisplayName("멤버가 가진 쿠폰목록을 조회한다.")
    void findCouponsByMember() {
        Long id1 = couponDao.createCoupon(COUPON1_10);
        Long id2 = couponDao.createCoupon(COUPON2_20);
        Member member = new Member("email", "password");
        Long memberId = memberDao.addMember(member);
        String sql = "INSERT INTO coupon_box (id, member_id, coupon_id) values (?, ? ,?)";
        jdbcTemplate.update(sql, 1L, memberId, id1);
        jdbcTemplate.update(sql, 2L, memberId, id2);

        assertThat(couponDao.findByMemberId(memberId)).hasSize(2);
    }

}