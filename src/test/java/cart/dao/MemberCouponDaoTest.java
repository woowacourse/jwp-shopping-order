package cart.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class MemberCouponDaoTest {
    private final MemberCouponDao memberCouponDao;

    @Autowired
    private MemberCouponDaoTest(JdbcTemplate jdbcTemplate) {
        memberCouponDao = new MemberCouponDao(jdbcTemplate);
    }

    @Test
    @DisplayName("사용자가 쿠폰을 가지고 있는지 확인한다.")
    void checkByMemberIdCouponId() {
        Assertions.assertThat(memberCouponDao.checkByMemberIdCouponId(1L, 1L)).isTrue();
    }

    @Test
    @DisplayName("쿠폰을 지급한다.")
    void create() {
        memberCouponDao.create(2L, 1L);
        Assertions.assertThat(memberCouponDao.checkByMemberIdCouponId(2L, 1L)).isTrue();
    }

    @Test
    @DisplayName("사용자의 모든 쿠폰은 찾는다.")
    void findByMemberId() {
        Assertions.assertThat(memberCouponDao.findByMemberId(1L)).hasSize(3);
    }

    @Test
    @DisplayName("쿠폰을 지운다.")
    void delete() {
        memberCouponDao.delete(1L, 1L);
        Assertions.assertThat(memberCouponDao.checkByMemberIdCouponId(1L, 1L)).isFalse();
    }
}
