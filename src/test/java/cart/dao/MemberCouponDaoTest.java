package cart.dao;

import cart.domain.member.MemberCoupon;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.stream.Collectors;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberCouponDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private MemberCouponDao memberCouponDao;

    @BeforeEach
    void setUp() {
        memberCouponDao = new MemberCouponDao(jdbcTemplate);
    }

    @Test
    void findByMemberId() {
        Long memberCouponId1 = memberCouponDao.create(1L, 1L);
        Long memberCouponId2 = memberCouponDao.create(1L, 2L);

        List<MemberCoupon> memberCoupons = memberCouponDao.findByMemberId(1L);

        List<Long> memberCouponIds = memberCoupons.stream().map(MemberCoupon::getId).collect(Collectors.toList());

        Assertions.assertThat(memberCouponIds).contains(memberCouponId1, memberCouponId2);
    }

    @Test
    void create() {
        Long memberCouponId1 = memberCouponDao.create(1L, 1L);
        Long memberCouponId2 = memberCouponDao.create(1L, 3L);
        List<MemberCoupon> memberCoupons = memberCouponDao.findByIds(List.of(memberCouponId1, memberCouponId2));

        List<Long> memberCouponIds = memberCoupons.stream().map(MemberCoupon::getId).collect(Collectors.toList());

        Assertions.assertThat(memberCouponIds).contains(memberCouponId1, memberCouponId2);
    }

    @Test
    void updateCoupon() {
        Long memberCouponId1 = memberCouponDao.create(1L, 1L);
        Long memberCouponId2 = memberCouponDao.create(1L, 3L);
        List<MemberCoupon> memberCoupons = memberCouponDao.findByIds(List.of(memberCouponId1, memberCouponId2));
        List<MemberCoupon> usedCoupons = memberCoupons.stream().map(MemberCoupon::use).collect(Collectors.toList());
        memberCouponDao.updateCoupon(usedCoupons, 1L);
        List<MemberCoupon> coupons = memberCouponDao.findByIds(List.of(memberCouponId1, memberCouponId2));
        List<Boolean> collect = coupons.stream().map(MemberCoupon::isUsed).collect(Collectors.toList());
        Assertions.assertThat(collect).isEqualTo(List.of(true, true));
    }
}
