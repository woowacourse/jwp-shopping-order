package cart.dao;

import cart.domain.member.MemberCoupon;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@JdbcTest
class MemberCouponDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private MemberCouponDao memberCouponDao;

    @BeforeEach
    void setUp() {
        memberCouponDao = new MemberCouponDao(jdbcTemplate);
    }

    @Test
    void createAndFindByIds() {
        Long memberCouponId1 = memberCouponDao.create(1L, 1L);
        Long memberCouponId2 = memberCouponDao.create(1L, 3L);
        List<MemberCoupon> memberCoupons = memberCouponDao.findByIds(List.of(memberCouponId1, memberCouponId2));

        Assertions.assertThat(memberCoupons).hasSize(2);
    }
}
